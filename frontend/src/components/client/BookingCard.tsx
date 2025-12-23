import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { formatDate, formatPrice } from '@/lib/formatUtils'
import { getDaysBetween } from '@/lib/dateUtils'
import { Calendar, MapPin, Users } from 'lucide-react'
import type { BookingResponse } from '@/types/api'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { bookingsApi } from '@/api/bookings'
import { useState } from 'react'

interface BookingCardProps {
  booking: BookingResponse
}

const statusLabels: Record<string, string> = {
  CONFIRMED: 'Подтверждено',
  CHECKED_IN: 'Заселен',
  CHECKED_OUT: 'Выселен',
  CANCELLED: 'Отменено',
}

const statusColors: Record<string, 'default' | 'secondary' | 'destructive'> = {
  CONFIRMED: 'default',
  CHECKED_IN: 'default',
  CHECKED_OUT: 'secondary',
  CANCELLED: 'destructive',
}

export const BookingCard: React.FC<BookingCardProps> = ({ booking }) => {
  const queryClient = useQueryClient()
  const [isCancelling, setIsCancelling] = useState(false)

  const days = getDaysBetween(booking.checkInDate, booking.checkOutDate)
  const totalPrice = days * Number(booking.room.roomType.basePrice)

  const cancelMutation = useMutation({
    mutationFn: () => bookingsApi.cancelBooking(booking.bookingId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['myBookings'] })
    },
    onError: () => {
      setIsCancelling(false)
    },
  })

  const handleCancel = () => {
    if (window.confirm('Вы уверены, что хотите отменить бронирование?')) {
      setIsCancelling(true)
      cancelMutation.mutate()
    }
  }

  const canCancel = booking.status === 'CONFIRMED'

  return (
    <Card>
      <CardHeader>
        <div className="flex items-start justify-between">
          <div>
            <CardTitle>Номер {booking.room.roomNumber}</CardTitle>
            <CardDescription>{booking.room.roomType.typeName}</CardDescription>
          </div>
          <Badge variant={statusColors[booking.status] || 'default'}>
            {statusLabels[booking.status] || booking.status}
          </Badge>
        </div>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="grid gap-4 md:grid-cols-2">
          <div className="flex items-center text-sm text-muted-foreground">
            <Calendar className="mr-2 h-4 w-4" />
            <div>
              <div>Заезд: {formatDate(booking.checkInDate)}</div>
              <div>Выезд: {formatDate(booking.checkOutDate)}</div>
            </div>
          </div>
          <div className="flex items-center text-sm text-muted-foreground">
            <Users className="mr-2 h-4 w-4" />
            <span>{days} ночей</span>
          </div>
        </div>

        <div className="flex items-center justify-between pt-4 border-t">
          <div>
            <div className="text-sm text-muted-foreground">Общая стоимость</div>
            <div className="text-xl font-bold">{formatPrice(totalPrice)}</div>
          </div>
          {canCancel && (
            <Button
              variant="destructive"
              onClick={handleCancel}
              disabled={cancelMutation.isPending || isCancelling}
            >
              Отменить
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  )
}

