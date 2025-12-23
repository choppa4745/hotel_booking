import { Badge } from '@/components/ui/badge'

const statusLabels: Record<string, string> = {
  CONFIRMED: 'Подтверждено',
  CHECKED_IN: 'Заселен',
  CHECKED_OUT: 'Выселен',
  CANCELLED: 'Отменено',
}

const statusColors: Record<string, 'default' | 'secondary' | 'destructive'> = {
  CONFIRMED: 'default',
  CHECKED_IN: 'default',
  CHECKED_OUT: 'secondary',
  CANCELLED: 'destructive',
}

interface StatusBadgeProps {
  status: string
}

export const StatusBadge: React.FC<StatusBadgeProps> = ({ status }) => {
  return (
    <Badge variant={statusColors[status] || 'default'}>
      {statusLabels[status] || status}
    </Badge>
  )
}

