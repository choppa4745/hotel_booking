import { ReactNode } from 'react'
import { useAuthStore } from '@/store/authStore'

interface RoleGuardProps {
  children: ReactNode
  allowedRoles: ('ADMIN' | 'OPERATOR' | 'CLIENT')[]
  fallback?: ReactNode
}

export const RoleGuard: React.FC<RoleGuardProps> = ({
  children,
  allowedRoles,
  fallback = null,
}) => {
  const { user } = useAuthStore()

  if (!user || !allowedRoles.includes(user.role)) {
    return <>{fallback}</>
  }

  return <>{children}</>
}

