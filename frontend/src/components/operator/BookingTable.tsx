import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { formatDate } from '@/lib/formatUtils'
import { BookingActions } from './BookingActions'
import { StatusBadge } from './StatusBadge'
import type { BookingResponse } from '@/types/api'

interface BookingTableProps {
  bookings: BookingResponse[]
}

export const BookingTable: React.FC<BookingTableProps> = ({ bookings }) => {
  return (
    <Card>
      <CardContent className="p-0">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-muted">
              <tr>
                <th className="px-4 py-3 text-left text-sm font-medium">ID</th>
                <th className="px-4 py-3 text-left text-sm font-medium">Гость</th>
                <th className="px-4 py-3 text-left text-sm font-medium">Номер</th>
                <th className="px-4 py-3 text-left text-sm font-medium">Даты</th>
                <th className="px-4 py-3 text-left text-sm font-medium">Статус</th>
                <th className="px-4 py-3 text-left text-sm font-medium">Действия</th>
              </tr>
            </thead>
            <tbody>
              {bookings.map((booking) => (
                <tr key={booking.bookingId} className="border-t">
                  <td className="px-4 py-3 text-sm">
                    {booking.bookingId.slice(0, 8)}...
                  </td>
                  <td className="px-4 py-3 text-sm">
                    {booking.guest.firstName} {booking.guest.lastName}
                  </td>
                  <td className="px-4 py-3 text-sm">
                    {booking.room.roomNumber}
                  </td>
                  <td className="px-4 py-3 text-sm">
                    <div>{formatDate(booking.checkInDate)}</div>
                    <div className="text-muted-foreground">
                      {formatDate(booking.checkOutDate)}
                    </div>
                  </td>
                  <td className="px-4 py-3">
                    <StatusBadge status={booking.status} />
                  </td>
                  <td className="px-4 py-3">
                    <BookingActions booking={booking} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </CardContent>
    </Card>
  )
}

