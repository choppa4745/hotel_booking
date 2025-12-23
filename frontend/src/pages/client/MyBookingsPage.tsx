import { useQuery } from '@tanstack/react-query'
import { bookingsApi } from '@/api/bookings'
import { Header } from '@/components/layout/Header'
import { Footer } from '@/components/layout/Footer'
import { BookingCard } from '@/components/client/BookingCard'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { Card, CardContent } from '@/components/ui/card'

export const ClientMyBookingsPage: React.FC = () => {
  const { data: bookings, isLoading } = useQuery({
    queryKey: ['myBookings'],
    queryFn: () => bookingsApi.getMyBookings(),
  })

  return (
    <div className="flex min-h-screen flex-col">
      <Header />
      <main className="container mx-auto flex-1 px-4 py-8">
        <div className="mb-6">
          <h1 className="text-3xl font-bold">Мои бронирования</h1>
          <p className="text-muted-foreground">
            Управляйте своими бронированиями
          </p>
        </div>

        {isLoading ? (
          <div className="flex justify-center py-12">
            <LoadingSpinner size={48} />
          </div>
        ) : bookings && bookings.length > 0 ? (
          <div className="space-y-4">
            {bookings.map((booking) => (
              <BookingCard key={booking.bookingId} booking={booking} />
            ))}
          </div>
        ) : (
          <Card>
            <CardContent className="p-12 text-center">
              <p className="text-muted-foreground">
                У вас пока нет бронирований
              </p>
            </CardContent>
          </Card>
        )}
      </main>
      <Footer />
    </div>
  )
}

