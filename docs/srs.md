# db connect

## Thiết kế

```sh
                    DB Client
                        │
        ┌───────────────┴────────────────┐
        │                                │
        ▼                                ▼
Config Database                  User Database
(lưu setting)                    (database thực)
        │                                │
     SQLite                      PostgreSQL
                                 MySQL
                                 Oracle
                                 SQL Server
                                 SQLite
```

```sh
Frontend
     │
     ▼
ConnectionController
     │
     ▼
ConnectionService
     │
     ├─────────────► SavedConnectionRepository
     │                     │
     │                     ▼
     │                 config.db
     │
     ▼
ConnectionManager
     │
     ▼
PostgreSQL / MySQL / Oracle ...
```

## Luồng hoạt động

```sh
Home
 │
 │ POST /connect/create
 ↓
Connected
 │
 ├── GET /metadata/tables/{sessionId}
 │
 ↓
DB Client
 │
 ├── click table
 │      └── GET /metadata/columns/{sessionId}/{table}
 │
 ├── Run
 │      └── POST /query
 │
 ├── Run Script
 │      └── POST /query/script
 │
 └── Disconnect
        └── DELETE /connect/{sessionId}
```
