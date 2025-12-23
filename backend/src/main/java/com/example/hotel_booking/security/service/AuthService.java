package com.example.hotel_booking.security.service;

import com.example.hotel_booking.security.SecurityUserPrincipal;
import com.example.hotel_booking.security.dto.*;
import com.example.hotel_booking.security.service.JwtService;
import com.example.hotel_booking.auth.dto.AuthResponse;
import com.example.hotel_booking.user.entity.Role;
import com.example.hotel_booking.user.entity.User;
import com.example.hotel_booking.user.repository.UserRepository;
import com.example.hotel_booking.staff.repository.EmployeeRepository;
import com.example.hotel_booking.user.repository.GuestRepository;
import com.example.hotel_booking.user.service.GuestService;
import com.example.hotel_booking.user.dto.request.GuestRequest;
import com.example.hotel_booking.user.dto.response.GuestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // если репы есть — лучше валидировать ссылку на employee/guest
    private final EmployeeRepository employeeRepository;
    private final GuestRepository guestRepository;
    private final GuestService guestService;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        validateAssociation(req);

        // Для CLIENT автоматически создаем Guest, если не указан
        UUID guestId = req.getGuestId();
        if (req.getRole() == Role.CLIENT && guestId == null) {
            // Проверяем, существует ли уже Guest с таким email
            guestId = guestRepository.findByEmail(req.getEmail())
                    .map(guest -> guest.getGuestId())
                    .orElseGet(() -> {
                        // Создаем нового Guest
                        GuestRequest guestRequest = new GuestRequest();
                        guestRequest.setEmail(req.getEmail());
                        // Используем username как firstName, lastName будет пустым (можно обновить позже)
                        guestRequest.setFirstName(req.getUsername());
                        guestRequest.setLastName("User");
                        GuestResponse createdGuest = guestService.createGuest(guestRequest);
                        return createdGuest.getGuestId();
                    });
        }

        User u = new User();
        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        u.setRole(req.getRole());
        u.setGuestId(guestId);
        u.setEmployeeId(req.getEmployeeId());
        u.setIsActive(true);
        u.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(u);

        String token = jwtService.generateToken(
                saved.getUsername(),
                saved.getRole(),
                saved.getEmployeeId(),
                saved.getGuestId()
        );

        return new AuthResponse(token, saved.getUsername(), saved.getRole(), saved.getEmployeeId(), saved.getGuestId());
    }

    public AuthResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsernameOrEmail(), req.getPassword())
        );

        // subject = usernameOrEmail; мы внутри userdetails умеем искать по username или email
        String subject = auth.getName();

        // грузим пользователя чтобы отдать role/ids и сформировать токен
        var principal = (SecurityUserPrincipal) auth.getPrincipal();

        String token = jwtService.generateToken(
                principal.getUsername(),
                principal.getRole(),
                principal.getEmployeeId(),
                principal.getGuestId()
        );

        return new AuthResponse(token, principal.getUsername(), principal.getRole(), principal.getEmployeeId(), principal.getGuestId());
    }

    private void validateAssociation(RegisterRequest req) {
        Role role = req.getRole();

        if (role == Role.CLIENT) {
            if (req.getEmployeeId() != null) {
                throw new IllegalArgumentException("CLIENT must NOT have employeeId");
            }
            // Если guestId не указан, создадим Guest автоматически
            if (req.getGuestId() == null) {
                // Guest будет создан в методе register перед сохранением User
                return;
            }
            // Если guestId указан, проверяем его существование
            if (!guestRepository.existsById(req.getGuestId())) {
                throw new IllegalArgumentException("Guest not found: " + req.getGuestId());
            }
        } else { // ADMIN or OPERATOR
            if (req.getEmployeeId() == null || req.getGuestId() != null) {
                throw new IllegalArgumentException(role + " must have employeeId and must NOT have guestId");
            }
            if (!employeeRepository.existsById(req.getEmployeeId())) {
                throw new IllegalArgumentException("Employee not found: " + req.getEmployeeId());
            }
        }
    }
}
