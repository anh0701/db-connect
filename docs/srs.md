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

[x] GET `/metadata/tables/{sessionId}`: Lấy danh sách các bảng trong database của một connection/session.

[x] GET `/metadata/columns/{sessionId}/{tableName}`: Lấy danh sách các cột của một bảng.

[ ] GET `/metadata/schemas/{sessionId}`: Lấy danh sách schema của database.

[ ] GET `/metadata/tables/{sessionId}?schema=public`: Lấy bảng theo schema cụ thể.

[ ] GET `/metadata/columns/{sessionId}/{schema}/{table}`: Lấy column của một bảng thuộc schema cụ thể.

### 2. Query

[x] POST `/query`: Chạy một câu SQL.

[ ] POST `/query/script`: Chạy nhiều câu SQL liên tiếp.

[ ] POST `/query/explain`: Xem execution plan của SQL.

### 3. Table data

[ ] GET `/data/{sessionId}/{tableName}`: Lấy dữ liệu của bảng.

[ ] GET `/data/{sessionId}/{tableName}?page=0&size=100`: Lấy dữ liệu có phân trang.

### 4. Session

[x] DELETE `/connect/{sessionId}`: Đóng database connection.

[x] GET `/connect/sessions`: Lấy tất cả session hiện đang hoạt động.

[ ] GET `/connect/{sessionId}`: Lấy thông tin một session cụ thể.  // optional

### 5. Database actions

[x] GET `/metadata/tables/{sessionId}`: Lấy danh sách các bảng trong database của một connection/session.

[ ] POST `/metadata/refresh/{sessionId}`: Refresh metadata.  // optional

### 6. Saved Connections

[ ] GET    `/saved-connections`: Lấy tất cả connection đã lưu.

[ ] GET    `/saved-connections/{id}`: Lấy chi tiết một saved connection.

[ ] POST   `/saved-connections`: Lưu một database connection mới.

[ ] PUT    `/saved-connections/{id}`: Cập nhật connection.

[ ] DELETE `/saved-connections/{id}`: Xóa saved connection.

### 7. Settings

[ ] GET `/settings`: Lấy toàn bộ application settings.

[ ] GET `/settings/save_connection`: Lấy trạng thái tự động lưu connection.

[ ] PUT `/settings/save_connection`: Thay đổi trạng thái tự động lưu connection.
