import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { roomsApi } from '@/api/rooms'
import { Header } from '@/components/layout/Header'
import { Footer } from '@/components/layout/Footer'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { RoomCard } from '@/components/client/RoomCard'
import { Search, Calendar } from 'lucide-react'
import { format } from 'date-fns'

export const ClientHomePage: React.FC = () => {
  const navigate = useNavigate()
  const [checkIn, setCheckIn] = useState<string>('')
  const [checkOut, setCheckOut] = useState<string>('')

  const { data: rooms, isLoading } = useQuery({
    queryKey: ['availableRooms'],
    queryFn: () => roomsApi.getAvailableRooms(),
  })

  const handleSearch = () => {
    if (checkIn && checkOut) {
      navigate(`/client/rooms?checkIn=${checkIn}&checkOut=${checkOut}`)
    } else {
      navigate('/client/rooms')
    }
  }

  const today = format(new Date(), 'yyyy-MM-dd')
  const tomorrow = format(new Date(Date.now() + 86400000), 'yyyy-MM-dd')

  return (
    <div className="flex min-h-screen flex-col">
      <Header />
      <main className="flex-1">
        {/* Hero Section */}
        <section className="bg-gradient-to-r from-orange-600 to-orange-700 py-20 text-white">
          <div className="container mx-auto px-4">
            <h1 className="mb-4 text-4xl font-bold">Добро пожаловать в наш отель</h1>
            <p className="mb-8 text-xl">Найдите идеальный номер для вашего отдыха</p>
            
            {/* Search Form */}
            <Card className="bg-white/95">
              <CardContent className="p-6">
                <div className="grid gap-4 md:grid-cols-3">
                  <div>
                    <label className="mb-2 block text-sm font-medium text-gray-700">
                      Дата заезда
                    </label>
                    <Input
                      type="date"
                      min={today}
                      value={checkIn}
                      onChange={(e) => setCheckIn(e.target.value)}
                      className="w-full"
                    />
                  </div>
                  <div>
                    <label className="mb-2 block text-sm font-medium text-gray-700">
                      Дата выезда
                    </label>
                    <Input
                      type="date"
                      min={checkIn || tomorrow}
                      value={checkOut}
                      onChange={(e) => setCheckOut(e.target.value)}
                      className="w-full"
                    />
                  </div>
                  <div className="flex items-end">
                    <Button onClick={handleSearch} className="w-full" size="lg">
                      <Search className="mr-2 h-4 w-4" />
                      Найти номер
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </section>

        {/* Available Rooms */}
        <section className="container mx-auto px-4 py-12">
          <h2 className="mb-6 text-3xl font-bold">Доступные номера</h2>
          
          {isLoading ? (
            <div className="flex justify-center py-12">
              <LoadingSpinner size={48} />
            </div>
          ) : rooms && rooms.length > 0 ? (
            <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
              {rooms.slice(0, 6).map((room) => (
                <RoomCard key={room.roomId} room={room} />
              ))}
            </div>
          ) : (
            <Card>
              <CardContent className="p-12 text-center">
                <p className="text-muted-foreground">
                  На данный момент нет доступных номеров
                </p>
              </CardContent>
            </Card>
          )}

          {rooms && rooms.length > 0 && (
            <div className="mt-8 text-center">
              <Button
                variant="outline"
                onClick={() => navigate('/client/rooms')}
                size="lg"
              >
                Посмотреть все номера
              </Button>
            </div>
          )}
        </section>
      </main>
      <Footer />
    </div>
  )
}

