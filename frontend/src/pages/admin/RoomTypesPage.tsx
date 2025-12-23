import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { roomTypesApi } from '@/api/roomTypes'
import { Header } from '@/components/layout/Header'
import { Sidebar } from '@/components/layout/Sidebar'
import { Footer } from '@/components/layout/Footer'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { RoomTypeForm } from '@/components/admin/RoomTypeForm'
import { DataTable } from '@/components/admin/DataTable'
import { Plus, Edit, Trash2 } from 'lucide-react'
import type { RoomTypeResponse } from '@/types/api'
import { formatPrice } from '@/lib/formatUtils'

export const AdminRoomTypesPage: React.FC = () => {
  const [isFormOpen, setIsFormOpen] = useState(false)
  const [editingType, setEditingType] = useState<RoomTypeResponse | null>(null)
  const queryClient = useQueryClient()

  const { data: roomTypes, isLoading } = useQuery({
    queryKey: ['roomTypes'],
    queryFn: () => roomTypesApi.getAllRoomTypes(),
  })

  const deleteMutation = useMutation({
    mutationFn: (id: string) => roomTypesApi.deleteRoomType(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['roomTypes'] })
    },
  })

  const handleEdit = (type: RoomTypeResponse) => {
    setEditingType(type)
    setIsFormOpen(true)
  }

  const handleCreate = () => {
    setEditingType(null)
    setIsFormOpen(true)
  }

  const handleClose = () => {
    setIsFormOpen(false)
    setEditingType(null)
  }

  const columns = [
    { key: 'typeName', label: 'Название' },
    { key: 'basePrice', label: 'Цена' },
    { key: 'maxGuests', label: 'Макс. гостей' },
    { key: 'description', label: 'Описание' },
    { key: 'actions', label: 'Действия' },
  ]

  const tableData = roomTypes?.map((type) => ({
    ...type,
    basePrice: formatPrice(Number(type.basePrice)),
    actions: (
      <div className="flex gap-2">
        <Button
          size="sm"
          variant="outline"
          onClick={() => handleEdit(type)}
        >
          <Edit className="h-4 w-4" />
        </Button>
        <Button
          size="sm"
          variant="destructive"
          onClick={() => {
            if (window.confirm('Удалить тип номера?')) {
              deleteMutation.mutate(type.typeId)
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
              <h1 className="text-3xl font-bold">Управление типами номеров</h1>
              <p className="text-muted-foreground">Создание и редактирование типов номеров</p>
            </div>
            <Button onClick={handleCreate}>
              <Plus className="mr-2 h-4 w-4" />
              Добавить тип
            </Button>
          </div>

          {isFormOpen && (
            <Card className="mb-6">
              <CardContent className="p-6">
                <RoomTypeForm
                  roomType={editingType || undefined}
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

