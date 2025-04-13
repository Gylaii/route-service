```
route-service/
├── build.gradle.kts
├── docker-compose.yml   # Локальное окружение с PostgreSQL и KeyDB
├── Dockerfile           # Для контейнеризации
├── src/
│   ├── Application.kt   # Точка входа (main)
│   ├── config/          # Конфигурации
│   ├── api/             # HTTP контроллеры
│   ├── domain/          # Бизнес-модели
│   ├── service/         # Бизнес-логика
│   ├── repository/      # Доступ к данным
│   └── messaging/       # Взаимодействие с KeyDB
└── resources/
    └── application.conf # Конфигурационный файл
```
