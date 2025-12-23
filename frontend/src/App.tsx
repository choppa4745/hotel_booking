import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { ErrorBoundary } from '@/components/common/ErrorBoundary'
import { useAuthStore } from '@/store/authStore'
import { LoginPage } from '@/pages/auth/LoginPage'
import { RegisterPage } from '@/pages/auth/RegisterPage'
import { ProtectedRoute } from '@/components/auth/ProtectedRoute'

// Client pages
import { ClientHomePage } from '@/pages/client/HomePage'
import { ClientRoomsPage } from '@/pages/client/RoomsPage'
import { ClientRoomDetailPage } from '@/pages/client/RoomDetailPage'
import { ClientMyBookingsPage } from '@/pages/client/MyBookingsPage'
import { ClientProfilePage } from '@/pages/client/ProfilePage'

// Operator pages
import { OperatorDashboardPage } from '@/pages/operator/DashboardPage'
import { OperatorBookingsPage } from '@/pages/operator/BookingsPage'

// Admin pages
import { AdminDashboardPage } from '@/pages/admin/DashboardPage'
import { AdminRoomsPage } from '@/pages/admin/RoomsPage'
import { AdminRoomTypesPage } from '@/pages/admin/RoomTypesPage'
import { AdminBookingsPage } from '@/pages/admin/BookingsPage'
import { AdminGuestsPage } from '@/pages/admin/GuestsPage'

const RootRedirect = () => {
  const { isAuthenticated, user } = useAuthStore()
  
  if (!isAuthenticated) {
    return <Navigate to="/auth/login" replace />
  }
  
  if (user?.role === 'ADMIN') {
    return <Navigate to="/admin/dashboard" replace />
  } else if (user?.role === 'OPERATOR') {
    return <Navigate to="/operator/dashboard" replace />
  } else {
    return <Navigate to="/client/home" replace />
  }
}

function App() {
  return (
    <ErrorBoundary>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<RootRedirect />} />
          
          {/* Auth routes */}
          <Route path="/auth/login" element={<LoginPage />} />
          <Route path="/auth/register" element={<RegisterPage />} />
          
          {/* Client routes */}
          <Route
            path="/client/*"
            element={
              <ProtectedRoute allowedRoles={['CLIENT', 'OPERATOR', 'ADMIN']}>
                <Routes>
                  <Route path="home" element={<ClientHomePage />} />
                  <Route path="rooms" element={<ClientRoomsPage />} />
                  <Route path="rooms/:id" element={<ClientRoomDetailPage />} />
                  <Route path="bookings" element={<ClientMyBookingsPage />} />
                  <Route path="profile" element={<ClientProfilePage />} />
                </Routes>
              </ProtectedRoute>
            }
          />
          
          {/* Operator routes */}
          <Route
            path="/operator/*"
            element={
              <ProtectedRoute allowedRoles={['OPERATOR', 'ADMIN']}>
                <Routes>
                  <Route path="dashboard" element={<OperatorDashboardPage />} />
                  <Route path="bookings" element={<OperatorBookingsPage />} />
                </Routes>
              </ProtectedRoute>
            }
          />
          
          {/* Admin routes */}
          <Route
            path="/admin/*"
            element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <Routes>
                  <Route path="dashboard" element={<AdminDashboardPage />} />
                  <Route path="rooms" element={<AdminRoomsPage />} />
                  <Route path="room-types" element={<AdminRoomTypesPage />} />
                  <Route path="bookings" element={<AdminBookingsPage />} />
                  <Route path="guests" element={<AdminGuestsPage />} />
                </Routes>
              </ProtectedRoute>
            }
          />
          
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </ErrorBoundary>
  )
}

export default App

