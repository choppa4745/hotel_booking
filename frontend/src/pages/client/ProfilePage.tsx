import { useQuery } from '@tanstack/react-query'
import { useAuthStore } from '@/store/authStore'
import { guestsApi } from '@/api/guests'
import { Header } from '@/components/layout/Header'
import { Footer } from '@/components/layout/Footer'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { LoadingSpinner } from '@/components/common/LoadingSpinner'
import { formatDate } from '@/lib/formatUtils'
import { User } from 'lucide-react'

export const ClientProfilePage: React.FC = () => {
  const { user } = useAuthStore()

  const { data: guest, isLoading } = useQuery({
    queryKey: ['guest', user?.guestId],
    queryFn: () => guestsApi.getGuestById(user?.guestId!),
    enabled: !!user?.guestId,
  })

  return (
    <div className="flex min-h-screen flex-col">
      <Header />
      <main className="container mx-auto flex-1 px-4 py-8">
        <div className="mb-6">
          <h1 className="text-3xl font-bold">Профиль</h1>
          <p className="text-muted-foreground">Информация о вашем профиле</p>
        </div>

        {isLoading ? (
          <div className="flex justify-center py-12">
            <LoadingSpinner size={48} />
          </div>
        ) : guest ? (
          <Card>
            <CardHeader>
              <div className="flex items-center space-x-4">
                <div className="flex h-16 w-16 items-center justify-center rounded-full bg-primary/10">
                  <User className="h-8 w-8 text-primary" />
                </div>
                <div>
                  <CardTitle>
                    {guest.firstName} {guest.lastName}
                  </CardTitle>
                  <CardDescription>{guest.email}</CardDescription>
                </div>
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div>
                <div className="text-sm font-medium text-muted-foreground">Телефон</div>
                <div className="text-lg">{guest.phone || 'Не указан'}</div>
              </div>
              {guest.passportNumber && (
                <div>
                  <div className="text-sm font-medium text-muted-foreground">Паспорт</div>
                  <div className="text-lg">{guest.passportNumber}</div>
                </div>
              )}
              {guest.dateOfBirth && (
                <div>
                  <div className="text-sm font-medium text-muted-foreground">Дата рождения</div>
                  <div className="text-lg">{formatDate(guest.dateOfBirth)}</div>
                </div>
              )}
            </CardContent>
          </Card>
        ) : (
          <Card>
            <CardContent className="p-12 text-center">
              <p className="text-muted-foreground">Профиль не найден</p>
            </CardContent>
          </Card>
        )}
      </main>
      <Footer />
    </div>
  )
}

