import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { bookingsApi } from '@/api/bookings'
import { useAuthStore } from '@/store/authStore'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { formatPrice } from '@/lib/formatUtils'
import { getDaysBetween } from '@/lib/dateUtils'
import type { RoomResponse } from '@/types/api'
import { useNavigate } from 'react-router-dom'

const bookingSchema = z.object({
  checkInDate: z.string().min(1, 'Обязательное поле'),
  checkOutDate: z.string().min(1, 'Обязательное поле'),
}).refine((data) => {
  const checkIn = new Date(data.checkInDate)
  const checkOut = new Date(data.checkOutDate)
  return checkOut > checkIn
}, {
  message: 'Дата выезда должна быть позже даты заезда',
  path: ['checkOutDate'],
})

type BookingFormData = z.infer<typeof bookingSchema>

interface BookingFormProps {
  room: RoomResponse
}

export const BookingForm: React.FC<BookingFormProps> = ({ room }) => {
  const navigate = useNavigate()
  const { user } = useAuthStore()
  const queryClient = useQueryClient()
  const [error, setError] = useState<string | null>(null)

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<BookingFormData>({
    resolver: zodResolver(bookingSchema),
  })

  const checkInDate = watch('checkInDate')
  const checkOutDate = watch('checkOutDate')

  const days = checkInDate && checkOutDate
    ? getDaysBetween(new Date(checkInDate), new Date(checkOutDate))
    : 0

  const totalPrice = days * Number(room.roomType.basePrice)

  const mutation = useMutation({
    mutationFn: (data: BookingFormData) => {
      if (!user?.guestId) {
        throw new Error('Необходимо войти в систему')
      }
      return bookingsApi.createBooking({
        guestId: user.guestId,
        roomId: room.roomId,
        checkInDate: data.checkInDate,
        checkOutDate: data.checkOutDate,
      })
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['bookings'] })
      navigate('/client/bookings')
    },
    onError: (err: any) => {
      setError(err.response?.data?.message || 'Ошибка при создании бронирования')
    },
  })

  const onSubmit = (data: BookingFormData) => {
    setError(null)
    mutation.mutate(data)
  }

  const today = new Date().toISOString().split('T')[0]

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="space-y-2">
        <Label htmlFor="checkInDate">Дата заезда</Label>
        <Input
          id="checkInDate"
          type="date"
          min={today}
          {...register('checkInDate')}
        />
        {errors.checkInDate && (
          <p className="text-sm text-red-500">{errors.checkInDate.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="checkOutDate">Дата выезда</Label>
        <Input
          id="checkOutDate"
          type="date"
          min={checkInDate || today}
          {...register('checkOutDate')}
        />
        {errors.checkOutDate && (
          <p className="text-sm text-red-500">{errors.checkOutDate.message}</p>
        )}
      </div>

      {days > 0 && (
        <div className="rounded-md bg-muted p-4 space-y-2">
          <div className="flex justify-between text-sm">
            <span>Количество ночей:</span>
            <span className="font-medium">{days}</span>
          </div>
          <div className="flex justify-between text-sm">
            <span>Цена за ночь:</span>
            <span className="font-medium">{formatPrice(Number(room.roomType.basePrice))}</span>
          </div>
          <div className="flex justify-between pt-2 border-t font-bold">
            <span>Итого:</span>
            <span className="text-primary">{formatPrice(totalPrice)}</span>
          </div>
        </div>
      )}

      {error && (
        <div className="rounded-md bg-red-50 p-3 text-sm text-red-800">
          {error}
        </div>
      )}

      <Button
        type="submit"
        className="w-full"
        disabled={mutation.isPending || !user?.guestId}
      >
        {mutation.isPending ? (
          <>
            <LoadingSpinner className="mr-2" size={16} />
            Бронирование...
          </>
        ) : (
          'Забронировать'
        )}
      </Button>

      {!user?.guestId && (
        <p className="text-sm text-muted-foreground text-center">
          Необходимо войти в систему для бронирования
        </p>
      )}
    </form>
  )
}

