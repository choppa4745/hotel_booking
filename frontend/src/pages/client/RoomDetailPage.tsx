import { useParams, useNavigate } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { roomsApi } from '@/api/rooms'
import { Header } from '@/components/layout/Header'
import { Footer } from '@/components/layout/Footer'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { BookingForm } from '@/components/client/BookingForm'
import { formatPrice } from '@/lib/formatUtils'
import { Bed, Users, Wifi, ArrowLeft } from 'lucide-react'
import { Badge } from '@/components/ui/badge'

export const ClientRoomDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()

  const { data: room, isLoading } = useQuery({
    queryKey: ['room', id],
    queryFn: () => roomsApi.getRoomById(id!),
    enabled: !!id,
  })

  if (isLoading) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <LoadingSpinner size={48} />
      </div>
    )
  }

  if (!room) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <Card>
          <CardContent className="p-12 text-center">
            <p className="text-muted-foreground">Номер не найден</p>
            <Button onClick={() => navigate('/client/rooms')} className="mt-4">
              Вернуться к каталогу
            </Button>
          </CardContent>
        </Card>
      </div>
    )
  }

  return (
    <div className="flex min-h-screen flex-col">
      <Header />
      <main className="container mx-auto flex-1 px-4 py-8">
        <Button
          variant="ghost"
          onClick={() => navigate('/client/rooms')}
          className="mb-6"
        >
          <ArrowLeft className="mr-2 h-4 w-4" />
          Назад к каталогу
        </Button>

        <div className="grid gap-8 lg:grid-cols-2">
          <div>
            <div className="mb-6 h-96 rounded-lg bg-gradient-to-br from-orange-100 to-orange-200 flex items-center justify-center">
              <Bed className="h-32 w-32 text-orange-500" />
            </div>

            <Card>
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div>
                    <CardTitle className="text-2xl">Номер {room.roomNumber}</CardTitle>
                    <CardDescription className="text-lg">
                      {room.roomType.typeName}
                    </CardDescription>
                  </div>
                  <Badge variant={room.isAvailable ? 'default' : 'secondary'}>
                    {room.isAvailable ? 'Доступен' : 'Занят'}
                  </Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-4">
                <div>
                  <h3 className="mb-2 font-semibold">Описание</h3>
                  <p className="text-muted-foreground">
                    {room.roomType.description || 'Уютный номер для комфортного отдыха'}
                  </p>
                </div>

                <div className="flex items-center text-muted-foreground">
                  <Users className="mr-2 h-5 w-5" />
                  <span>До {room.roomType.maxGuests} гостей</span>
                </div>

                <div className="flex items-center text-muted-foreground">
                  <Bed className="mr-2 h-5 w-5" />
                  <span>Этаж {room.floor}</span>
                </div>

                {room.amenities.length > 0 && (
                  <div>
                    <h3 className="mb-2 font-semibold">Удобства</h3>
                    <div className="flex flex-wrap gap-2">
                      {room.amenities.map((amenity) => (
                        <Badge key={amenity.amenityId} variant="outline">
                          {amenity.amenityName}
                        </Badge>
                      ))}
                    </div>
                  </div>
                )}
              </CardContent>
            </Card>
          </div>

          <div>
            <Card className="sticky top-4">
              <CardHeader>
                <CardTitle>Бронирование</CardTitle>
                <CardDescription>
                  Выберите даты заезда и выезда
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="mb-6">
                  <div className="text-3xl font-bold text-primary">
                    {formatPrice(room.roomType.basePrice)}
                  </div>
                  <div className="text-sm text-muted-foreground">за ночь</div>
                </div>

                {room.isAvailable ? (
                  <BookingForm room={room} />
                ) : (
                  <div className="rounded-md bg-muted p-4 text-center text-sm text-muted-foreground">
                    Номер временно недоступен для бронирования
                  </div>
                )}
              </CardContent>
            </Card>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  )
}

