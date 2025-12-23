import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { roomTypesApi } from '@/api/roomTypes'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import type { RoomTypeResponse, RoomTypeRequest } from '@/types/api'

const roomTypeSchema = z.object({
  typeName: z.string().min(1, 'Обязательное поле'),
  basePrice: z.number().min(0.01, 'Цена должна быть больше 0'),
  maxGuests: z.number().min(1, 'Минимум 1 гость'),
  description: z.string().optional(),
})

type RoomTypeFormData = z.infer<typeof roomTypeSchema>

interface RoomTypeFormProps {
  roomType?: RoomTypeResponse
  onSuccess: () => void
  onCancel: () => void
}

export const RoomTypeForm: React.FC<RoomTypeFormProps> = ({
  roomType,
  onSuccess,
  onCancel,
}) => {
  const queryClient = useQueryClient()

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RoomTypeFormData>({
    resolver: zodResolver(roomTypeSchema),
    defaultValues: roomType
      ? {
          typeName: roomType.typeName,
          basePrice: Number(roomType.basePrice),
          maxGuests: roomType.maxGuests,
          description: roomType.description || '',
        }
      : undefined,
  })

  const mutation = useMutation({
    mutationFn: (data: RoomTypeRequest) => {
      if (roomType) {
        return roomTypesApi.updateRoomType(roomType.typeId, data)
      }
      return roomTypesApi.createRoomType(data)
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['roomTypes'] })
      onSuccess()
    },
  })

  const onSubmit = (data: RoomTypeFormData) => {
    mutation.mutate({
      typeName: data.typeName,
      basePrice: data.basePrice,
      maxGuests: data.maxGuests,
      description: data.description || '',
    })
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid gap-4 md:grid-cols-2">
        <div className="space-y-2">
          <Label htmlFor="typeName">Название типа</Label>
          <Input id="typeName" {...register('typeName')} />
          {errors.typeName && (
            <p className="text-sm text-red-500">{errors.typeName.message}</p>
          )}
        </div>

        <div className="space-y-2">
          <Label htmlFor="basePrice">Базовая цена (₽)</Label>
          <Input
            id="basePrice"
            type="number"
            step="0.01"
            {...register('basePrice', { valueAsNumber: true })}
          />
          {errors.basePrice && (
            <p className="text-sm text-red-500">{errors.basePrice.message}</p>
          )}
        </div>

        <div className="space-y-2">
          <Label htmlFor="maxGuests">Максимум гостей</Label>
          <Input
            id="maxGuests"
            type="number"
            {...register('maxGuests', { valueAsNumber: true })}
          />
          {errors.maxGuests && (
            <p className="text-sm text-red-500">{errors.maxGuests.message}</p>
          )}
        </div>

        <div className="space-y-2 md:col-span-2">
          <Label htmlFor="description">Описание</Label>
          <textarea
            id="description"
            className="flex min-h-[80px] w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
            {...register('description')}
          />
        </div>
      </div>

      <div className="flex gap-2">
        <Button type="submit" disabled={mutation.isPending}>
          {mutation.isPending ? (
            <>
              <LoadingSpinner className="mr-2" size={16} />
              Сохранение...
            </>
          ) : (
            roomType ? 'Обновить' : 'Создать'
          )}
        </Button>
        <Button type="button" variant="outline" onClick={onCancel}>
          Отмена
        </Button>
      </div>
    </form>
  )
}

