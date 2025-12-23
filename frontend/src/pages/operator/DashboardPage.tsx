import { useQuery } from '@tanstack/react-query'
import { bookingsApi } from '@/api/bookings'
import { Header } from '@/components/layout/Header'
import { Sidebar } from '@/components/layout/Sidebar'
import { Footer } from '@/components/layout/Footer'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { Calendar, CheckCircle, Clock, XCircle } from 'lucide-react'

export const OperatorDashboardPage: React.FC = () => {
  const { data: activeBookings } = useQuery({
    queryKey: ['activeBookings'],
    queryFn: () => bookingsApi.getActiveBookings(),
  })

  const { data: allBookings } = useQuery({
    queryKey: ['allBookings'],
    queryFn: () => bookingsApi.getAllBookings(),
  })

  const confirmedCount = allBookings?.filter(b => b.status === 'CONFIRMED').length || 0
  const checkedInCount = allBookings?.filter(b => b.status === 'CHECKED_IN').length || 0
  const checkedOutCount = allBookings?.filter(b => b.status === 'CHECKED_OUT').length || 0
  const cancelledCount = allBookings?.filter(b => b.status === 'CANCELLED').length || 0

  return (
    <div className="flex min-h-screen flex-col">
      <Header />
      <div className="flex flex-1">
        <Sidebar />
        <main className="flex-1 p-8">
          <div className="mb-6">
            <h1 className="text-3xl font-bold">Панель оператора</h1>
            <p className="text-muted-foreground">Управление бронированиями</p>
          </div>

          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4 mb-8">
            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Подтверждено</CardTitle>
                <CheckCircle className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{confirmedCount}</div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Заселены</CardTitle>
                <Clock className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{checkedInCount}</div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Выселены</CardTitle>
                <CheckCircle className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{checkedOutCount}</div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                <CardTitle className="text-sm font-medium">Отменено</CardTitle>
                <XCircle className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{cancelledCount}</div>
              </CardContent>
            </Card>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>Активные бронирования</CardTitle>
              <CardDescription>Текущие заселения</CardDescription>
            </CardHeader>
            <CardContent>
              {activeBookings && activeBookings.length > 0 ? (
                <div className="space-y-4">
                  {activeBookings.slice(0, 5).map((booking) => (
                    <div key={booking.bookingId} className="flex items-center justify-between p-4 border rounded-lg">
                      <div>
                        <div className="font-medium">Номер {booking.room.roomNumber}</div>
                        <div className="text-sm text-muted-foreground">
                          {booking.guest.firstName} {booking.guest.lastName}
                        </div>
                      </div>
                      <div className="text-sm text-muted-foreground">
                        {booking.status}
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-muted-foreground text-center py-8">
                  Нет активных бронирований
                </p>
              )}
            </CardContent>
          </Card>
        </main>
      </div>
      <Footer />
    </div>
  )
}

