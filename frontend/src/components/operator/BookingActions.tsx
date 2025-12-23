import { useMutation, useQueryClient } from '@tanstack/react-query'
import { bookingsApi } from '@/api/bookings'
import { Button } from '@/components/ui/button'
import { LogIn, LogOut } from 'lucide-react'
import type { BookingResponse } from '@/types/api'

interface BookingActionsProps {
  booking: BookingResponse
}

export const BookingActions: React.FC<BookingActionsProps> = ({ booking }) => {
  const queryClient = useQueryClient()

  const checkInMutation = useMutation({
    mutationFn: () => bookingsApi.checkInBooking(booking.bookingId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['allBookings'] })
    },
  })

  const checkOutMutation = useMutation({
    mutationFn: () => bookingsApi.checkOutBooking(booking.bookingId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['allBookings'] })
    },
  })

  const canCheckIn = booking.status === 'CONFIRMED'
  const canCheckOut = booking.status === 'CHECKED_IN'

  if (!canCheckIn && !canCheckOut) {
    return <span className="text-sm text-muted-foreground">-</span>
  }

  return (
    <div className="flex gap-2">
      {canCheckIn && (
        <Button
          size="sm"
          onClick={() => checkInMutation.mutate()}
          disabled={checkInMutation.isPending}
        >
          <LogIn className="mr-1 h-3 w-3" />
          Заселить
        </Button>
      )}
      {canCheckOut && (
        <Button
          size="sm"
          variant="outline"
          onClick={() => checkOutMutation.mutate()}
          disabled={checkOutMutation.isPending}
        >
          <LogOut className="mr-1 h-3 w-3" />
          Выселить
        </Button>
      )}
    </div>
  )
}

