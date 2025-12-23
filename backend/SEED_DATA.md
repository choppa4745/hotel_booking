# Seed Data - Учетные данные для входа

После выполнения миграций база данных будет заполнена тестовыми данными.

## Учетные данные пользователей

### Администратор

- **Username:** `admin`
- **Password:** `admin123`
- **Email:** `admin@hotel.com`
- **Роль:** ADMIN
- **Сотрудник:** Robert Brown (Manager)

### Оператор

- **Username:** `operator1`
- **Password:** `operator123`
- **Email:** `emily.wilson@hotel.com`
- **Роль:** OPERATOR
- **Сотрудник:** Emily Wilson (Receptionist)

### Клиенты

- **Username:** `client1`
- **Password:** `client123`
- **Email:** `john.smith@email.com`
- **Роль:** CLIENT
- **Гость:** John Smith

- **Username:** `client2`
- **Password:** `client123`
- **Email:** `maria.garcia@email.com`
- **Роль:** CLIENT
- **Гость:** Maria Garcia

## Тестовые данные

### Типы номеров

- STANDARD - 100.00, макс. 2 гостя
- DELUXE - 150.00, макс. 3 гостя
- SUITE - 250.00, макс. 4 гостя
- FAMILY - 180.00, макс. 5 гостей

### Номера

- 101, 102, 103 (STANDARD, этаж 1)
- 201, 202, 203 (DELUXE, этаж 2)
- 301, 302 (SUITE, этаж 3)
- 401, 402 (FAMILY, этаж 4)

### Удобства

- WiFi, TV, Air Conditioning, Mini Bar, Safe

### Сотрудники

- Robert Brown - Manager (robert.brown@hotel.com)
- Emily Wilson - Receptionist (emily.wilson@hotel.com)
- Michael Davis - Housekeeper (michael.davis@hotel.com)

### Гости

- John Smith (john.smith@email.com)
- Maria Garcia (maria.garcia@email.com)
- David Johnson (david.johnson@email.com)
- Sarah Wilson (sarah.wilson@email.com)

## Примечание

### ❌ Можно ли создать админа через регистрацию?

**НЕТ, через текущую форму регистрации нельзя создать аккаунт ADMIN или OPERATOR.**

**Причины:**

1. Для ADMIN/OPERATOR требуется `employeeId` (связь с таблицей `employees`)
2. Форма регистрации на фронтенде не содержит поля `employeeId`
3. Валидация в `AuthService.validateAssociation()` требует:
   - Для ADMIN/OPERATOR: `employeeId` обязателен, `guestId` должен быть NULL
   - Для CLIENT: `guestId` создается автоматически, `employeeId` должен быть NULL

**Решение:**

- ✅ Аккаунт админа уже создан через миграцию `004-create-admin-account.xml`
- ✅ Для создания новых ADMIN/OPERATOR аккаунтов:
  1. Сначала создать сотрудника (Employee) через админ-панель или API
  2. Затем создать пользователя с соответствующим `employee_id` через админ-панель или напрямую в БД

### Миграции

Все seed данные создаются через миграции Liquibase:

- `002-insert-test-data.xml` - основные тестовые данные
- `004-create-admin-account.xml` - гарантированное создание админского аккаунта

Миграции применяются автоматически при запуске приложения.
