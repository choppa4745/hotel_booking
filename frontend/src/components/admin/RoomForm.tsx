import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { roomsApi } from '@/api/rooms'
import { roomTypesApi } from '@/api/roomTypes'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import type { RoomResponse, RoomRequest } from '@/types/api'

const roomSchema = z.object({
  roomNumber: z.string().min(1, 'Обязательное поле'),
  typeId: z.string().min(1, 'Обязательное поле'),
  floor: z.number().min(1, 'Минимум 1 этаж'),
  isAvailable: z.boolean().default(true),
})

type RoomFormData = z.infer<typeof roomSchema>

interface RoomFormProps {
  room?: RoomResponse
  onSuccess: () => void
  onCancel: () => void
}

export const RoomForm: React.FC<RoomFormProps> = ({ room, onSuccess, onCancel }) => {
  const queryClient = useQueryClient()

  const { data: roomTypes } = useQuery({
    queryKey: ['roomTypes'],
    queryFn: () => roomTypesApi.getAllRoomTypes(),
  })

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RoomFormData>({
    resolver: zodResolver(roomSchema),
    defaultValues: room
      ? {
          roomNumber: room.roomNumber,
          typeId: room.roomType.typeId,
          floor: room.floor,
          isAvailable: room.isAvailable,
        }
      : {
          isAvailable: true,
        },
  })

  const mutation = useMutation({
    mutationFn: (data: RoomRequest) => {
      if (room) {
        return roomsApi.updateRoom(room.roomId, data)
      }
      return roomsApi.createRoom(data)
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['rooms'] })
      onSuccess()
    },
  })

  const onSubmit = (data: RoomFormData) => {
    mutation.mutate({
      roomNumber: data.roomNumber,
      typeId: data.typeId,
      floor: data.floor,
      isAvailable: data.isAvailable,
    })
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid gap-4 md:grid-cols-2">
        <div className="space-y-2">
          <Label htmlFor="roomNumber">Номер комнаты</Label>
          <Input
            id="roomNumber"
            {...register('roomNumber')}
          />
          {errors.roomNumber && (
            <p className="text-sm text-red-500">{errors.roomNumber.message}</p>
          )}
        </div>

        <div className="space-y-2">
          <Label htmlFor="typeId">Тип номера</Label>
          <select
            id="typeId"
            className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
            {...register('typeId')}
          >
            <option value="">Выберите тип</option>
            {roomTypes?.map((type) => (
              <option key={type.typeId} value={type.typeId}>
                {type.typeName}
              </option>
            ))}
          </select>
          {errors.typeId && (
            <p className="text-sm text-red-500">{errors.typeId.message}</p>
          )}
        </div>

        <div className="space-y-2">
          <Label htmlFor="floor">Этаж</Label>
          <Input
            id="floor"
            type="number"
            {...register('floor', { valueAsNumber: true })}
          />
          {errors.floor && (
            <p className="text-sm text-red-500">{errors.floor.message}</p>
          )}
        </div>

        <div className="space-y-2">
          <Label htmlFor="isAvailable">Доступность</Label>
          <select
            id="isAvailable"
            className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
            {...register('isAvailable', {
              setValueAs: (v) => v === 'true',
            })}
          >
            <option value="true">Доступен</option>
            <option value="false">Занят</option>
          </select>
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
            room ? 'Обновить' : 'Создать'
          )}
        </Button>
        <Button type="button" variant="outline" onClick={onCancel}>
          Отмена
        </Button>
      </div>
    </form>
  )
}

