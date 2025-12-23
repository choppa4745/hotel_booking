import { apiClient } from './client'
import type { RoomResponse, RoomRequest } from '@/types/api'

export const roomsApi = {
  getAllRooms: async (): Promise<RoomResponse[]> => {
    const response = await apiClient.get<RoomResponse[]>('/rooms')
    return response.data
  },

  getRoomById: async (id: string): Promise<RoomResponse> => {
    const response = await apiClient.get<RoomResponse>(`/rooms/${id}`)
    return response.data
  },

  getRoomByNumber: async (roomNumber: string): Promise<RoomResponse> => {
    const response = await apiClient.get<RoomResponse>(`/rooms/number/${roomNumber}`)
    return response.data
  },

  getAvailableRooms: async (): Promise<RoomResponse[]> => {
    const response = await apiClient.get<RoomResponse[]>('/rooms/available')
    return response.data
  },

  createRoom: async (data: RoomRequest): Promise<RoomResponse> => {
    const response = await apiClient.post<RoomResponse>('/rooms', data)
    return response.data
  },

  updateRoom: async (id: string, data: RoomRequest): Promise<RoomResponse> => {
    const response = await apiClient.put<RoomResponse>(`/rooms/${id}`, data)
    return response.data
  },

  deleteRoom: async (id: string): Promise<void> => {
    await apiClient.delete(`/rooms/${id}`)
  },

  updateRoomAvailability: async (id: string, available: boolean): Promise<RoomResponse> => {
    const response = await apiClient.patch<RoomResponse>(
      `/rooms/${id}/availability`,
      null,
      { params: { available } }
    )
    return response.data
  },
}

