import { formatDate } from './dateUtils'

export const formatPrice = (price: number): string => {
  return new Intl.NumberFormat('ru-RU', {
    style: 'currency',
    currency: 'RUB',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(price)
}

export const formatPhone = (phone: string): string => {
  // Форматирование телефона в формат +7 (XXX) XXX-XX-XX
  const cleaned = phone.replace(/\D/g, '')
  if (cleaned.length === 11 && cleaned.startsWith('7')) {
    return `+7 (${cleaned.slice(1, 4)}) ${cleaned.slice(4, 7)}-${cleaned.slice(7, 9)}-${cleaned.slice(9)}`
  }
  return phone
}

export const formatPassport = (passport: string): string => {
  // Форматирование паспорта: серия и номер
  if (passport.length === 10) {
    return `${passport.slice(0, 4)} ${passport.slice(4)}`
  }
  return passport
}

export { formatDate, formatDateTime } from './dateUtils'

