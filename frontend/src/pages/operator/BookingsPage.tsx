import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { bookingsApi } from '@/api/bookings'
import { Header } from '@/components/layout/Header'
import { Sidebar } from '@/components/layout/Sidebar'
import { Footer } from '@/components/layout/Footer'
import { BookingTable } from '@/components/operator/BookingTable'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { Card, CardContent } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Search } from 'lucide-react'

export const OperatorBookingsPage: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState<string>('')

  const { data: bookings, isLoading } = useQuery({
    queryKey: ['allBookings'],
    queryFn: () => bookingsApi.getAllBookings(),
  })

  const filteredBookings = bookings?.filter((booking) => {
    const matchesSearch =
      booking.guest.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      booking.guest.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      booking.room.roomNumber.toLowerCase().includes(searchTerm.toLowerCase())
    
    const matchesStatus = !statusFilter || booking.status === statusFilter

    return matchesSearch && matchesStatus
  })

  return (
    <div className="flex min-h-screen flex-col">
      <Header />
      <div className="flex flex-1">
        <Sidebar />
        <main className="flex-1 p-8">
          <div className="mb-6">
            <h1 className="text-3xl font-bold">Управление бронированиями</h1>
            <p className="text-muted-foreground">Просмотр и управление всеми бронированиями</p>
          </div>

          <Card className="mb-6">
            <CardContent className="p-4">
              <div className="flex gap-4">
                <div className="flex-1 relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    placeholder="Поиск по гостю или номеру..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="pl-10"
                  />
                </div>
                <select
                  className="flex h-10 rounded-md border border-input bg-background px-3 py-2 text-sm"
                  value={statusFilter}
                  onChange={(e) => setStatusFilter(e.target.value)}
                >
                  <option value="">Все статусы</option>
                  <option value="CONFIRMED">Подтверждено</option>
                  <option value="CHECKED_IN">Заселен</option>
                  <option value="CHECKED_OUT">Выселен</option>
                  <option value="CANCELLED">Отменено</option>
                </select>
              </div>
            </CardContent>
          </Card>

          {isLoading ? (
            <div className="flex justify-center py-12">
              <LoadingSpinner size={48} />
            </div>
          ) : filteredBookings && filteredBookings.length > 0 ? (
            <BookingTable bookings={filteredBookings} />
          ) : (
            <Card>
              <CardContent className="p-12 text-center">
                <p className="text-muted-foreground">Бронирования не найдены</p>
              </CardContent>
            </Card>
          )}
        </main>
      </div>
      <Footer />
    </div>
  )
}

