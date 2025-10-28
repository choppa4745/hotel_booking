#!/bin/bash

echo "Starting Hotel Booking System database initialization..."

# Запуск контейнеров
docker-compose up -d

echo "Waiting for PostgreSQL to be ready..."
sleep 10

# Проверка статуса контейнеров
docker-compose ps

echo "Database initialization completed!"
echo "PostgreSQL is available on localhost:5432"
echo "pgAdmin is available on http://localhost:8080"
echo "Credentials: admin@hotel.com / admin123"