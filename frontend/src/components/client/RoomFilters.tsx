import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Button } from '@/components/ui/button'
import { useQuery } from '@tanstack/react-query'
import { roomTypesApi } from '@/api/roomTypes'

interface RoomFiltersProps {
  checkIn?: string
  checkOut?: string
  onFilterChange: (filters: any) => void
}

export const RoomFilters: React.FC<RoomFiltersProps> = ({
  checkIn,
  checkOut,
  onFilterChange,
}) => {
  const [localCheckIn, setLocalCheckIn] = useState(checkIn || '')
  const [localCheckOut, setLocalCheckOut] = useState(checkOut || '')
  const [selectedType, setSelectedType] = useState<string>('')
  const [maxPrice, setMaxPrice] = useState<string>('')

  const { data: roomTypes } = useQuery({
    queryKey: ['roomTypes'],
    queryFn: () => roomTypesApi.getAllRoomTypes(),
  })

  const handleApply = () => {
    onFilterChange({
      checkIn: localCheckIn,
      checkOut: localCheckOut,
      roomType: selectedType,
      maxPrice: maxPrice ? Number(maxPrice) : undefined,
    })
  }

  const handleReset = () => {
    setLocalCheckIn('')
    setLocalCheckOut('')
    setSelectedType('')
    setMaxPrice('')
    onFilterChange({})
  }

  const today = new Date().toISOString().split('T')[0]

  return (
    <Card>
      <CardHeader>
        <CardTitle>Фильтры</CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="space-y-2">
          <Label>Дата заезда</Label>
          <Input
            type="date"
            min={today}
            value={localCheckIn}
            onChange={(e) => setLocalCheckIn(e.target.value)}
          />
        </div>

        <div className="space-y-2">
          <Label>Дата выезда</Label>
          <Input
            type="date"
            min={localCheckIn || today}
            value={localCheckOut}
            onChange={(e) => setLocalCheckOut(e.target.value)}
          />
        </div>

        <div className="space-y-2">
          <Label>Тип номера</Label>
          <select
            className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
            value={selectedType}
            onChange={(e) => setSelectedType(e.target.value)}
          >
            <option value="">Все типы</option>
            {roomTypes?.map((type) => (
              <option key={type.typeId} value={type.typeId}>
                {type.typeName}
              </option>
            ))}
          </select>
        </div>

        <div className="space-y-2">
          <Label>Максимальная цена (₽)</Label>
          <Input
            type="number"
            placeholder="Не ограничено"
            value={maxPrice}
            onChange={(e) => setMaxPrice(e.target.value)}
          />
        </div>

        <div className="flex gap-2">
          <Button onClick={handleApply} className="flex-1">
            Применить
          </Button>
          <Button variant="outline" onClick={handleReset}>
            Сбросить
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}

