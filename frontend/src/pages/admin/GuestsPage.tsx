import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { guestsApi } from '@/api/guests'
import { Header } from '@/components/layout/Header'
import { Sidebar } from '@/components/layout/Sidebar'
import { Footer } from '@/components/layout/Footer'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { DataTable } from '@/components/admin/DataTable'
import { Trash2 } from 'lucide-react'
import { formatDate } from '@/lib/formatUtils'

export const AdminGuestsPage: React.FC = () => {
  const queryClient = useQueryClient()

  const { data: guests, isLoading } = useQuery({
    queryKey: ['guests'],
    queryFn: () => guestsApi.getAllGuests(),
  })

  const deleteMutation = useMutation({
    mutationFn: (id: string) => guestsApi.deleteGuest(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['guests'] })
    },
  })

  const columns = [
    { key: 'firstName', label: 'Имя' },
    { key: 'lastName', label: 'Фамилия' },
    { key: 'email', label: 'Email' },
    { key: 'phone', label: 'Телефон' },
    { key: 'passportNumber', label: 'Паспорт' },
    { key: 'dateOfBirth', label: 'Дата рождения' },
    { key: 'actions', label: 'Действия' },
  ]

  const tableData = guests?.map((guest) => ({
    ...guest,
    dateOfBirth: guest.dateOfBirth ? formatDate(guest.dateOfBirth) : '-',
    phone: guest.phone || '-',
    passportNumber: guest.passportNumber || '-',
    actions: (
      <Button
        size="sm"
        variant="destructive"
        onClick={() => {
          if (window.confirm('Удалить гостя?')) {
            deleteMutation.mutate(guest.guestId)
          }
        }}
      >
        <Trash2 className="h-4 w-4" />
      </Button>
    ),
  })) || []

  return (
    <div className="flex min-h-screen flex-col">
      <Header />
      <div className="flex flex-1">
        <Sidebar />
        <main className="flex-1 p-8">
          <div className="mb-6">
            <h1 className="text-3xl font-bold">Управление гостями</h1>
            <p className="text-muted-foreground">Просмотр всех гостей системы</p>
          </div>

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

