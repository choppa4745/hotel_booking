import { BookingTable } from '@/components/operator/BookingTable'
import { Header } from '@/components/layout/Header'
import { Sidebar } from '@/components/layout/Sidebar'
import { Footer } from '@/components/layout/Footer'
import { useQuery } from '@tanstack/react-query'
import { bookingsApi } from '@/api/bookings'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { Card, CardContent } from '@/components/ui/card'

export const AdminBookingsPage: React.FC = () => {
  const { data: bookings, isLoading } = useQuery({
    queryKey: ['allBookings'],
    queryFn: () => bookingsApi.getAllBookings(),
  })

  return (
    <div className="flex min-h-screen flex-col">
      <Header />
      <div className="flex flex-1">
        <Sidebar />
        <main className="flex-1 p-8">
          <div className="mb-6">
            <h1 className="text-3xl font-bold">Все бронирования</h1>
            <p className="text-muted-foreground">Просмотр всех бронирований системы</p>
          </div>

          {isLoading ? (
            <div className="flex justify-center py-12">
              <LoadingSpinner size={48} />
            </div>
          ) : bookings && bookings.length > 0 ? (
            <BookingTable bookings={bookings} />
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

