#!/bin/bash

echo "Running Liquibase migrations..."

# Запуск Liquibase вручную (если нужно)
docker run --rm \
  -v $(pwd)/liquibase:/liquibase/changelog \
  --network hotel-booking-system_hotel-network \
  liquibase/liquibase:4.18 \
  --defaults-file=/liquibase/changelog/liquibase.properties \
  update

echo "Liquibase migrations completed!"