import { apiClient } from './client'
import type { BookingResponse, BookingRequest } from '@/types/api'

export const bookingsApi = {
  getAllBookings: async (): Promise<BookingResponse[]> => {
    const response = await apiClient.get<BookingResponse[]>('/bookings')
    return response.data
  },

  getBookingById: async (id: string): Promise<BookingResponse> => {
    const response = await apiClient.get<BookingResponse>(`/bookings/${id}`)
    return response.data
  },

  getMyBookings: async (): Promise<BookingResponse[]> => {
    const response = await apiClient.get<BookingResponse[]>('/bookings/my')
    return response.data
  },

  getBookingsByGuest: async (guestId: string): Promise<BookingResponse[]> => {
    const response = await apiClient.get<BookingResponse[]>(`/bookings/guest/${guestId}`)
    return response.data
  },

  getBookingsByRoom: async (roomId: string): Promise<BookingResponse[]> => {
    const response = await apiClient.get<BookingResponse[]>(`/bookings/room/${roomId}`)
    return response.data
  },

  getBookingsByStatus: async (status: string): Promise<BookingResponse[]> => {
    const response = await apiClient.get<BookingResponse[]>(`/bookings/status/${status}`)
    return response.data
  },

  getActiveBookings: async (): Promise<BookingResponse[]> => {
    const response = await apiClient.get<BookingResponse[]>('/bookings/active')
    return response.data
  },

  createBooking: async (data: BookingRequest): Promise<BookingResponse> => {
    const response = await apiClient.post<BookingResponse>('/bookings', data)
    return response.data
  },

  checkInBooking: async (id: string): Promise<BookingResponse> => {
    const response = await apiClient.patch<BookingResponse>(`/bookings/${id}/check-in`)
    return response.data
  },

  checkOutBooking: async (id: string): Promise<BookingResponse> => {
    const response = await apiClient.patch<BookingResponse>(`/bookings/${id}/check-out`)
    return response.data
  },

  cancelBooking: async (id: string): Promise<void> => {
    await apiClient.patch(`/bookings/${id}/cancel`)
  },

  checkRoomAvailability: async (
    roomId: string,
    checkInDate: string,
    checkOutDate: string
  ): Promise<boolean> => {
    const response = await apiClient.get<boolean>('/bookings/availability', {
      params: { roomId, checkInDate, checkOutDate },
    })
    return response.data
  },
}

