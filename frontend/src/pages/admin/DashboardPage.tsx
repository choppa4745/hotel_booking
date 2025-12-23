import { useQuery } from '@tanstack/react-query'
import { bookingsApi } from '@/api/bookings'
import { roomsApi } from '@/api/rooms'
import { guestsApi } from '@/api/guests'
import { Header } from '@/components/layout/Header'
import { Sidebar } from '@/components/layout/Sidebar'
import { Footer } from '@/components/layout/Footer'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { Calendar, Bed, Users, DollarSign } from 'lucide-react'

export const AdminDashboardPage: React.FC = () => {
  const { data: bookings } = useQuery({
    queryKey: ['allBookings'],
    queryFn: () => bookingsApi.getAllBookings(),
  })

  const { data: rooms } = useQuery({
    queryKey: ['rooms'],
    queryFn: () => roomsApi.getAllRooms(),
  })

  const { data: guests } = useQuery({
    queryKey: ['guests'],
    queryFn: () => guestsApi.getAllGuests(),
  })

  const totalBookings = bookings?.length || 0
  const totalRooms = rooms?.length || 0
  const totalGuests = guests?.length || 0
  const availableRooms = rooms?.filter(r => r.isAvailable).length || 0

  return (
    <div className="flex min-h-screen flex-col">
      <Header />
      <div className="flex flex-1">
        <Sidebar />
        <main className="flex-1 p-8">
          <div className="mb-6">
            <h1 className="text-3xl font-bold">Панель администратора</h1>
            <p className="text-muted-foreground">Обзор системы</p>
          </div>

          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4 mb-8">
            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Всего бронирований</CardTitle>
                <Calendar className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{totalBookings}</div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Всего номеров</CardTitle>
                <Bed className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{totalRooms}</div>
                <p className="text-xs text-muted-foreground mt-1">
                  {availableRooms} доступно
                </p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Всего гостей</CardTitle>
                <Users className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{totalGuests}</div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Доступность</CardTitle>
                <DollarSign className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">
                  {totalRooms > 0 ? Math.round((availableRooms / totalRooms) * 100) : 0}%
                </div>
                <p className="text-xs text-muted-foreground mt-1">
                  номеров свободно
                </p>
              </CardContent>
            </Card>
          </div>
        </main>
      </div>
      <Footer />
    </div>
  )
}

