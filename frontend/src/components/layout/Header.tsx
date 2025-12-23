import { Link, useNavigate } from 'react-router-dom'
import { useAuthStore } from '@/store/authStore'
import { Button } from '@/components/ui/button'
import { LogOut, User, Home } from 'lucide-react'
import { RoleGuard } from '@/components/auth/RoleGuard'

export const Header: React.FC = () => {
  const { user, logout, isAuthenticated } = useAuthStore()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/auth/login')
  }

  if (!isAuthenticated) {
    return null
  }

  const getHomePath = () => {
    if (user?.role === 'ADMIN') return '/admin/dashboard'
    if (user?.role === 'OPERATOR') return '/operator/dashboard'
    return '/client/home'
  }

  return (
    <header className="border-b bg-white">
      <div className="container mx-auto flex h-16 items-center justify-between px-4">
        <Link to={getHomePath()} className="flex items-center space-x-2">
          <Home className="h-6 w-6" />
          <span className="text-xl font-bold">Отель</span>
        </Link>

        <nav className="flex items-center space-x-4">
          <RoleGuard allowedRoles={['CLIENT', 'OPERATOR', 'ADMIN']}>
            <Link to="/client/home">
              <Button variant="ghost">Комнаты</Button>
            </Link>
            <Link to="/client/bookings">
              <Button variant="ghost">Мои бронирования</Button>
            </Link>
          </RoleGuard>

          <RoleGuard allowedRoles={['OPERATOR', 'ADMIN']}>
            <Link to="/operator/dashboard">
              <Button variant="ghost">Панель оператора</Button>
            </Link>
          </RoleGuard>

          <RoleGuard allowedRoles={['ADMIN']}>
            <Link to="/admin/dashboard">
              <Button variant="ghost">Панель администратора</Button>
            </Link>
          </RoleGuard>

          <div className="flex items-center space-x-2">
            <User className="h-4 w-4" />
            <span className="text-sm">{user?.username}</span>
            <Button variant="ghost" size="icon" onClick={handleLogout}>
              <LogOut className="h-4 w-4" />
            </Button>
          </div>
        </nav>
      </div>
    </header>
  )
}

