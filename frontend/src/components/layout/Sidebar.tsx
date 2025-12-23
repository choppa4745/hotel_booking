import { Link, useLocation } from 'react-router-dom'
import { cn } from '@/lib/utils'
import { 
  LayoutDashboard, 
  Bed, 
  Calendar, 
  Users, 
  Home,
  Settings
} from 'lucide-react'
import { useAuthStore } from '@/store/authStore'

interface NavItem {
  title: string
  href: string
  icon: React.ComponentType<{ className?: string }>
  roles: ('ADMIN' | 'OPERATOR' | 'CLIENT')[]
}

const clientNavItems: NavItem[] = [
  { title: 'Главная', href: '/client/home', icon: Home, roles: ['CLIENT'] },
  { title: 'Комнаты', href: '/client/rooms', icon: Bed, roles: ['CLIENT'] },
  { title: 'Мои бронирования', href: '/client/bookings', icon: Calendar, roles: ['CLIENT'] },
  { title: 'Профиль', href: '/client/profile', icon: Settings, roles: ['CLIENT'] },
]

const operatorNavItems: NavItem[] = [
  { title: 'Дашборд', href: '/operator/dashboard', icon: LayoutDashboard, roles: ['OPERATOR'] },
  { title: 'Бронирования', href: '/operator/bookings', icon: Calendar, roles: ['OPERATOR'] },
]

const adminNavItems: NavItem[] = [
  { title: 'Дашборд', href: '/admin/dashboard', icon: LayoutDashboard, roles: ['ADMIN'] },
  { title: 'Комнаты', href: '/admin/rooms', icon: Bed, roles: ['ADMIN'] },
  { title: 'Типы комнат', href: '/admin/room-types', icon: Settings, roles: ['ADMIN'] },
  { title: 'Бронирования', href: '/admin/bookings', icon: Calendar, roles: ['ADMIN'] },
  { title: 'Гости', href: '/admin/guests', icon: Users, roles: ['ADMIN'] },
]

export const Sidebar: React.FC = () => {
  const location = useLocation()
  const { user } = useAuthStore()

  const getNavItems = (): NavItem[] => {
    if (user?.role === 'ADMIN') return adminNavItems
    if (user?.role === 'OPERATOR') return operatorNavItems
    return clientNavItems
  }

  const navItems = getNavItems()

  return (
    <aside className="w-64 border-r bg-white p-4">
      <nav className="space-y-2">
        {navItems.map((item) => {
          const Icon = item.icon
          const isActive = location.pathname === item.href
          
          return (
            <Link
              key={item.href}
              to={item.href}
              className={cn(
                "flex items-center space-x-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors",
                isActive
                  ? "bg-primary text-primary-foreground"
                  : "text-muted-foreground hover:bg-accent hover:text-accent-foreground"
              )}
            >
              <Icon className="h-5 w-5" />
              <span>{item.title}</span>
            </Link>
          )
        })}
      </nav>
    </aside>
  )
}

