import { useState, useEffect } from 'react'
import { useSearchParams } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { roomsApi } from '@/api/rooms'
import { Header } from '@/components/layout/Header'
import { Footer } from '@/components/layout/Footer'
import { RoomCard } from '@/components/client/RoomCard'
import { RoomFilters } from '@/components/client/RoomFilters'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { Card, CardContent } from '@/components/ui/card'
import type { RoomResponse } from '@/types/api'

export const ClientRoomsPage: React.FC = () => {
  const [searchParams] = useSearchParams()
  const checkIn = searchParams.get('checkIn') || ''
  const checkOut = searchParams.get('checkOut') || ''

  const { data: rooms, isLoading } = useQuery({
    queryKey: ['rooms'],
    queryFn: () => roomsApi.getAllRooms(),
  })

  const [filteredRooms, setFilteredRooms] = useState<RoomResponse[]>([])

  useEffect(() => {
    if (rooms) {
      let filtered = [...rooms]

      // Фильтр по датам (если указаны)
      if (checkIn && checkOut) {
        // Здесь можно добавить проверку доступности через API
        filtered = filtered.filter((room) => room.isAvailable)
      }

      setFilteredRooms(filtered)
    }
  }, [rooms, checkIn, checkOut])

  return (
    <div className="flex min-h-screen flex-col">
      <Header />
      <main className="container mx-auto flex-1 px-4 py-8">
        <div className="mb-6">
          <h1 className="text-3xl font-bold">Каталог номеров</h1>
          <p className="text-muted-foreground">
            Выберите идеальный номер для вашего отдыха
          </p>
        </div>

        <div className="grid gap-6 lg:grid-cols-4">
          <aside className="lg:col-span-1">
            <RoomFilters
              checkIn={checkIn}
              checkOut={checkOut}
              onFilterChange={(filters) => {
                // Обработка фильтров
                console.log('Filters:', filters)
              }}
            />
          </aside>

          <div className="lg:col-span-3">
            {isLoading ? (
              <div className="flex justify-center py-12">
                <LoadingSpinner size={48} />
              </div>
            ) : filteredRooms.length > 0 ? (
              <div className="grid gap-6 md:grid-cols-2">
                {filteredRooms.map((room) => (
                  <RoomCard key={room.roomId} room={room} />
                ))}
              </div>
            ) : (
              <Card>
                <CardContent className="p-12 text-center">
                  <p className="text-muted-foreground">
                    Номера не найдены по заданным критериям
                  </p>
                </CardContent>
              </Card>
            )}
          </div>
        </div>
      </main>
      <Footer />
    </div>
  )
}

