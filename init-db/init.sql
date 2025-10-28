-- Создание базы данных (если ещё не создана через переменные окружения)
CREATE DATABASE IF NOT EXISTS hotel_booking_system;

-- Подключение к базе данных и создание таблиц
\c hotel_booking_system;

CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
                                     email VARCHAR(100) UNIQUE NOT NULL,
                                     password VARCHAR(100) NOT NULL,
                                     role VARCHAR(20) NOT NULL DEFAULT 'USER',
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Добавьте остальные таблицы вашей системы...