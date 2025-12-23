import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { roomsApi } from '@/api/rooms'
import { Header } from '@/components/layout/Header'
import { Sidebar } from '@/components/layout/Sidebar'
import { Footer } from '@/components/layout/Footer'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { RoomForm } from '@/components/admin/RoomForm'
import { DataTable } from '@/components/admin/DataTable'
import { Plus, Edit, Trash2 } from 'lucide-react'
import type { RoomResponse } from '@/types/api'
import { Badge } from '@/components/ui/badge'

export const AdminRoomsPage: React.FC = () => {
  const [isFormOpen, setIsFormOpen] = useState(false)
  const [editingRoom, setEditingRoom] = useState<RoomResponse | null>(null)
  const queryClient = useQueryClient()

  const { data: rooms, isLoading } = useQuery({
    queryKey: ['rooms'],
    queryFn: () => roomsApi.getAllRooms(),
  })

  const deleteMutation = useMutation({
    mutationFn: (id: string) => roomsApi.deleteRoom(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['rooms'] })
    },
  })

  const handleEdit = (room: RoomResponse) => {
    setEditingRoom(room)
    setIsFormOpen(true)
  }

  const handleCreate = () => {
    setEditingRoom(null)
    setIsFormOpen(true)
  }

  const handleClose = () => {
    setIsFormOpen(false)
    setEditingRoom(null)
  }

  const columns = [
    { key: 'roomNumber', label: 'Номер' },
    { key: 'roomType', label: 'Тип' },
    { key: 'floor', label: 'Этаж' },
    { key: 'isAvailable', label: 'Статус' },
    { key: 'actions', label: 'Действия' },
  ]

  const tableData = rooms?.map((room) => ({
    ...room,
    roomType: room.roomType.typeName,
    isAvailable: (
      <Badge variant={room.isAvailable ? 'default' : 'secondary'}>
        {room.isAvailable ? 'Доступен' : 'Занят'}
      </Badge>
    ),
    actions: (
      <div className="flex gap-2">
        <Button
          size="sm"
          variant="outline"
          onClick={() => handleEdit(room)}
        >
          <Edit className="h-4 w-4" />
        </Button>
        <Button
          size="sm"
          variant="destructive"
          onClick={() => {
            if (window.confirm('Удалить номер?')) {
              deleteMutation.mutate(room.roomId)
            }
          }}
        >
          <Trash2 className="h-4 w-4" />
        </Button>
      </div>
    ),
  })) || []

  return (
    <div className="flex min-h-screen flex-col">
      <Header />
      <div className="flex flex-1">
        <Sidebar />
        <main className="flex-1 p-8">
          <div className="mb-6 flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold">Управление номерами</h1>
              <p className="text-muted-foreground">Создание и редактирование номеров</p>
            </div>
            <Button onClick={handleCreate}>
              <Plus className="mr-2 h-4 w-4" />
              Добавить номер
            </Button>
          </div>

          {isFormOpen && (
            <Card className="mb-6">
              <CardContent className="p-6">
                <RoomForm
                  room={editingRoom || undefined}
                  onSuccess={handleClose}
                  onCancel={handleClose}
                />
              </CardContent>
            </Card>
          )}

          {isLoading ? (
            <div className="flex justify-center py-12">
              <LoadingSpinner size={48} />
            </div>
          ) : (
            <DataTable columns={columns} data={tableData} />
          )}
        </main>
      </div>
      <Footer />
    </div>
  )
}

