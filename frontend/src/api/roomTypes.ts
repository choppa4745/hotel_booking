import { apiClient } from './client'
import type { RoomTypeResponse, RoomTypeRequest } from '@/types/api'

export const roomTypesApi = {
  getAllRoomTypes: async (): Promise<RoomTypeResponse[]> => {
    const response = await apiClient.get<RoomTypeResponse[]>('/room-types')
    return response.data
  },

  getRoomTypeById: async (id: string): Promise<RoomTypeResponse> => {
    const response = await apiClient.get<RoomTypeResponse>(`/room-types/${id}`)
    return response.data
  },

  getRoomTypeByName: async (name: string): Promise<RoomTypeResponse> => {
    const response = await apiClient.get<RoomTypeResponse>(`/room-types/name/${name}`)
    return response.data
  },

  createRoomType: async (data: RoomTypeRequest): Promise<RoomTypeResponse> => {
    const response = await apiClient.post<RoomTypeResponse>('/room-types', data)
    return response.data
  },

  updateRoomType: async (id: string, data: RoomTypeRequest): Promise<RoomTypeResponse> => {
    const response = await apiClient.put<RoomTypeResponse>(`/room-types/${id}`, data)
    return response.data
  },

  deleteRoomType: async (id: string): Promise<void> => {
    await apiClient.delete(`/room-types/${id}`)
  },
}

