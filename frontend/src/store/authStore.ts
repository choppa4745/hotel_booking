import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { AuthResponse } from '@/types/api'

interface User {
  username: string
  role: 'ADMIN' | 'OPERATOR' | 'CLIENT'
  employeeId?: string
  guestId?: string
}

interface AuthState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  setAuth: (authResponse: AuthResponse) => void
  logout: () => void
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      user: null,
      token: null,
      isAuthenticated: false,
      setAuth: (authResponse: AuthResponse) => {
        localStorage.setItem('accessToken', authResponse.accessToken)
        set({
          user: {
            username: authResponse.username,
            role: authResponse.role,
            employeeId: authResponse.employeeId,
            guestId: authResponse.guestId,
          },
          token: authResponse.accessToken,
          isAuthenticated: true,
        })
      },
      logout: () => {
        localStorage.removeItem('accessToken')
        set({
          user: null,
          token: null,
          isAuthenticated: false,
        })
      },
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        user: state.user,
        token: state.token,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
)

