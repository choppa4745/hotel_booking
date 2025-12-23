import { apiClient } from './client'
import type { GuestResponse, GuestRequest } from '@/types/api'

export const guestsApi = {
  getAllGuests: async (): Promise<GuestResponse[]> => {
    const response = await apiClient.get<GuestResponse[]>('/guests')
    return response.data
  },

  getGuestById: async (id: string): Promise<GuestResponse> => {
    const response = await apiClient.get<GuestResponse>(`/guests/${id}`)
    return response.data
  },

  getGuestByEmail: async (email: string): Promise<GuestResponse> => {
    const response = await apiClient.get<GuestResponse>(`/guests/email/${email}`)
    return response.data
  },

  getGuestByPassport: async (passportNumber: string): Promise<GuestResponse> => {
    const response = await apiClient.get<GuestResponse>(`/guests/passport/${passportNumber}`)
    return response.data
  },

  createGuest: async (data: GuestRequest): Promise<GuestResponse> => {
    const response = await apiClient.post<GuestResponse>('/guests', data)
    return response.data
  },

  updateGuest: async (id: string, data: GuestRequest): Promise<GuestResponse> => {
    const response = await apiClient.put<GuestResponse>(`/guests/${id}`, data)
    return response.data
  },

  deleteGuest: async (id: string): Promise<void> => {
    await apiClient.delete(`/guests/${id}`)
  },
}

