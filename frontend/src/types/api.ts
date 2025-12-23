// Auth types
export interface LoginRequest {
  usernameOrEmail: string
  password: string
}

export interface RegisterRequest {
  username: string
  email: string
  password: string
  role: 'ADMIN' | 'OPERATOR' | 'CLIENT'
  guestId?: string
  employeeId?: string
}

export interface AuthResponse {
  accessToken: string
  username: string
  role: 'ADMIN' | 'OPERATOR' | 'CLIENT'
  employeeId?: string
  guestId?: string
}

// Room types
export interface RoomTypeResponse {
  typeId: string
  typeName: string
  basePrice: number
  maxGuests: number
  description: string
  createdAt: string
}

export interface AmenityResponse {
  amenityId: string
  amenityName: string
  description: string
  createdAt: string
}

export interface RoomResponse {
  roomId: string
  roomNumber: string
  roomType: RoomTypeResponse
  floor: number
  isAvailable: boolean
  createdAt: string
  amenities: AmenityResponse[]
}

export interface RoomRequest {
  roomNumber: string
  typeId: string
  floor: number
  isAvailable?: boolean
  amenityIds?: string[]
}

export interface RoomTypeRequest {
  typeName: string
  basePrice: number
  maxGuests: number
  description: string
}

// Guest types
export interface GuestResponse {
  guestId: string
  firstName: string
  lastName: string
  email: string
  phone: string
  passportNumber: string
  dateOfBirth: string
  createdAt: string
}

export interface GuestRequest {
  firstName: string
  lastName: string
  email: string
  phone?: string
  passportNumber?: string
  dateOfBirth?: string
}

// Booking types
export interface BookingResponse {
  bookingId: string
  guest: GuestResponse
  room: RoomResponse
  checkInDate: string
  checkOutDate: string
  status: 'CONFIRMED' | 'CHECKED_IN' | 'CHECKED_OUT' | 'CANCELLED'
  createdAt: string
  updatedAt: string
  additionalGuests: GuestResponse[]
}

export interface BookingRequest {
  guestId: string
  roomId: string
  checkInDate: string
  checkOutDate: string
  additionalGuestIds?: string[]
}

