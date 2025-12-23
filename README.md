# Hotel Booking System

Система бронирования отелей с разделением ролей (CLIENT, OPERATOR, ADMIN).

## Технологии

### Backend
- Spring Boot 3
- PostgreSQL
- Liquibase для миграций
- JWT аутентификация
- Docker

### Frontend
- React 18 + TypeScript
- Vite
- Tailwind CSS
- shadcn/ui
- React Router
- TanStack Query
- Zustand

## Быстрый старт

### Запуск через Docker Compose (рекомендуется)

1. Убедитесь, что Docker и Docker Compose установлены

2. Запустите все сервисы:
```bash
docker-compose up -d
```

3. Дождитесь запуска всех сервисов (особенно backend, который ждет готовности PostgreSQL)

4. Откройте в браузере:
   - Frontend: http://localhost:5173
   - Backend API: http://localhost:8081
   - PostgreSQL: localhost:5443

### Остановка

```bash
docker-compose down
```

### Пересоздание с очисткой данных

```bash
docker-compose down -v
docker-compose up -d --build
```

## Структура проекта

```
.
├── backend/          # Spring Boot приложение
├── frontend/         # React приложение
└── docker-compose.yml # Общая конфигурация Docker Compose
```

## Учетные данные

См. `backend/SEED_DATA.md` для тестовых учетных данных.

## Разработка

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Порты

- Frontend: 5173 (dev) / 80 (docker)
- Backend: 8081
- PostgreSQL: 5443

## Переменные окружения

### Backend
- `SPRING_DATASOURCE_URL`: URL базы данных
- `SPRING_DATASOURCE_USERNAME`: Имя пользователя БД
- `SPRING_DATASOURCE_PASSWORD`: Пароль БД
- `APP_JWT_SECRET`: Секретный ключ для JWT (минимум 32 символа)
- `APP_JWT_ACCESS_TOKEN_TTL_MINUTES`: Время жизни токена

### Frontend
- `VITE_API_URL`: URL API бэкенда (по умолчанию `/api` для прокси через nginx)

