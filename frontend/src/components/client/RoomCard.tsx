import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { formatPrice } from '@/lib/formatUtils'
import { Bed, Users, Wifi } from 'lucide-react'
import type { RoomResponse } from '@/types/api'

interface RoomCardProps {
  room: RoomResponse
}

export const RoomCard: React.FC<RoomCardProps> = ({ room }) => {
  const navigate = useNavigate()

  return (
    <Card className="overflow-hidden transition-shadow hover:shadow-lg">
      <div className="h-48 bg-gradient-to-br from-orange-100 to-orange-200 flex items-center justify-center">
        <Bed className="h-16 w-16 text-orange-500" />
      </div>
      <CardHeader>
        <div className="flex items-start justify-between">
          <div>
            <CardTitle className="text-xl">Номер {room.roomNumber}</CardTitle>
            <CardDescription>{room.roomType.typeName}</CardDescription>
          </div>
          <Badge variant={room.isAvailable ? 'default' : 'secondary'}>
            {room.isAvailable ? 'Доступен' : 'Занят'}
          </Badge>
        </div>
      </CardHeader>
      <CardContent>
        <div className="space-y-3">
          <div className="flex items-center text-sm text-muted-foreground">
            <Users className="mr-2 h-4 w-4" />
            До {room.roomType.maxGuests} гостей
          </div>
          <div className="flex items-center text-sm text-muted-foreground">
            <Wifi className="mr-2 h-4 w-4" />
            {room.amenities.length} удобств
          </div>
          <div className="pt-2 border-t">
            <div className="text-2xl font-bold text-primary">
              {formatPrice(room.roomType.basePrice)}
            </div>
            <div className="text-sm text-muted-foreground">за ночь</div>
          </div>
          <Button
            className="w-full"
            onClick={() => navigate(`/client/rooms/${room.roomId}`)}
            disabled={!room.isAvailable}
          >
            {room.isAvailable ? 'Забронировать' : 'Недоступен'}
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}

