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

## API

### 1. Database Explorer

[x] GET /metadata/tables/{sessionId}

[x] GET /metadata/columns/{sessionId}/{tableName}

[ ] GET /metadata/schemas/{sessionId}

[ ] GET /metadata/tables/{sessionId}?schema=public

[ ] GET /metadata/columns/{sessionId}/{schema}/{table}

### 2. Query

[x] POST /query

[ ] POST /query/script

[ ] POST /query/explain

### 3. Table data

[ ] GET /data/{sessionId}/{tableName}

[ ] GET /data/{sessionId}/{tableName}?page=0&size=100

### 4. Session

[x] DELETE /connect/{sessionId}

[x] GET /connect/sessions

[ ] GET /connect/{sessionId}  // optional

### 5. Database actions

[x] GET /metadata/tables/{sessionId}

[ ] POST /metadata/refresh/{sessionId}  // optional

### 6. Saved Connections

[ ] GET    /saved-connections

[ ] GET    /saved-connections/{id}

[ ] POST   /saved-connections

[ ] PUT    /saved-connections/{id}

[ ] DELETE /saved-connections/{id}

### 7. Settings

[ ] GET /settings

[ ] GET /settings/save_connection

[ ] PUT /settings/save_connection
