# 👥 Sistema de Gestión de Usuarios y Roles

## Descripción General

Sistema completo de ABM (Alta, Baja, Modificación) de **Usuarios** y **Roles dinámicos** con permisos granulares. Los roles se pueden crear, editar y eliminar según las necesidades del tenant, permitiendo un control de acceso flexible y personalizable.

---

## 🏗️ Arquitectura

### Entidades Principales

1. **Role** - Roles dinámicos con permisos personalizados
2. **User** - Usuarios del sistema
3. **UserRole** - Tabla de relación many-to-many entre usuarios y roles

### Roles del Sistema (Predefinidos)

Los siguientes roles vienen precargados y no se pueden eliminar (`isSystem = true`):

| Código | Nombre | Descripción | Permisos |
|--------|--------|-------------|----------|
| `ADMIN` | Administrador | Acceso completo | `["*"]` |
| `SUPERVISOR` | Supervisor | Gestión de equipos | `users.*, leads.*, contacts.*, reports.read` |
| `AGENT` | Agente | Operación diaria | `leads.read/update, contacts.read/update, interactions.*` |
| `USER` | Usuario | Solo lectura | `leads.read, contacts.read` |

---

## 📋 Endpoints Disponibles

### Gestión de Roles (`/roles`)

```bash
# Listar todos los roles del tenant
GET /roles
Authorization: Bearer {token}
X-Tenant-Id: {tenant-id}

# Listar roles del sistema (predefinidos)
GET /roles/system

# Obtener un rol por ID
GET /roles/{id}

# Crear un nuevo rol (solo ADMIN)
POST /roles
{
  "name": "Supervisor de Ventas",
  "code": "SUPERVISOR_VENTAS",
  "description": "Supervisor del equipo de ventas",
  "permissions": [
    "leads.read",
    "leads.create",
    "leads.update",
    "contacts.read",
    "reports.read"
  ],
  "active": true
}

# Actualizar un rol (solo ADMIN)
PUT /roles/{id}
{
  "name": "Supervisor de Ventas Senior",
  "description": "Supervisor senior con más permisos",
  "permissions": [
    "leads.*",
    "contacts.*",
    "reports.*"
  ],
  "active": true
}

# Eliminar un rol (solo ADMIN, soft delete)
DELETE /roles/{id}
```

### Gestión de Usuarios (`/users`)

```bash
# Listar todos los usuarios (paginado, ADMIN/SUPERVISOR)
GET /users?page=0&size=10&sortBy=createdAt&sortDir=DESC

# Obtener un usuario por ID
GET /users/{id}

# Crear un nuevo usuario (ADMIN/SUPERVISOR)
POST /users
{
  "email": "nuevo.usuario@demo.com",
  "password": "password123",
  "firstName": "Nuevo",
  "lastName": "Usuario",
  "roleIds": [
    "r3333333-3333-3333-3333-333333333333"  // ID del rol Agente
  ]
}

# Actualizar un usuario (ADMIN/SUPERVISOR)
PUT /users/{id}
{
  "firstName": "Nombre Actualizado",
  "lastName": "Apellido Actualizado",
  "password": "nuevaPassword123",  // Opcional
  "enabled": true,
  "roleIds": [
    "r2222222-2222-2222-2222-222222222222"  // Cambiar a Supervisor
  ]
}

# Eliminar un usuario (solo ADMIN, deshabilita)
DELETE /users/{id}

# Asignar un rol a un usuario (ADMIN/SUPERVISOR)
POST /users/{userId}/roles/{roleId}

# Remover un rol de un usuario (ADMIN/SUPERVISOR)
DELETE /users/{userId}/roles/{roleId}

# Habilitar un usuario (solo ADMIN)
PATCH /users/{id}/enable

# Deshabilitar un usuario (solo ADMIN)
PATCH /users/{id}/disable
```

---

## 🎯 Ejemplos de Uso

### 1. Crear un Rol Personalizado

```bash
POST /roles
Authorization: Bearer {token}
X-Tenant-Id: a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d
Content-Type: application/json

{
  "name": "Gerente Regional",
  "code": "GERENTE_REGIONAL",
  "description": "Gerente con acceso a múltiples equipos",
  "permissions": [
    "users.read",
    "users.update",
    "leads.*",
    "contacts.*",
    "interactions.read",
    "reports.*",
    "custom-fields.read"
  ],
  "active": true
}
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Rol creado exitosamente",
  "data": {
    "id": "12345678-1234-1234-1234-123456789012",
    "tenantId": "a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d",
    "name": "Gerente Regional",
    "code": "GERENTE_REGIONAL",
    "description": "Gerente con acceso a múltiples equipos",
    "permissions": ["users.read", "users.update", "leads.*", ...],
    "active": true,
    "isSystem": false,
    "createdAt": "2025-10-16T14:30:00Z"
  }
}
```

### 2. Crear un Usuario con Múltiples Roles

```bash
POST /users
Authorization: Bearer {token}
X-Tenant-Id: a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d

{
  "email": "gerente@demo.com",
  "password": "SecurePass123!",
  "firstName": "Carlos",
  "lastName": "Rodríguez",
  "roleIds": [
    "r2222222-2222-2222-2222-222222222222",  // SUPERVISOR
    "12345678-1234-1234-1234-123456789012"   // GERENTE_REGIONAL
  ]
}
```

### 3. Listar Usuarios con Paginación

```bash
GET /users?page=0&size=20&sortBy=lastName&sortDir=ASC
```

**Respuesta:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": "11111111-1111-1111-1111-111111111111",
        "email": "admin@demo.com",
        "firstName": "Admin",
        "lastName": "Usuario",
        "enabled": true,
        "roles": [
          {
            "id": "r1111111-1111-1111-1111-111111111111",
            "code": "ADMIN",
            "name": "Administrador",
            "permissions": ["*"]
          }
        ],
        "createdAt": "2025-10-16T10:00:00Z"
      }
    ],
    "totalElements": 15,
    "totalPages": 1,
    "size": 20,
    "number": 0
  }
}
```

### 4. Actualizar Roles de un Usuario

```bash
# Método 1: Actualizar completamente (reemplaza todos los roles)
PUT /users/33333333-3333-3333-3333-333333333333
{
  "roleIds": [
    "r2222222-2222-2222-2222-222222222222",  // SUPERVISOR
    "r3333333-3333-3333-3333-333333333333"   // AGENT (mantiene ambos)
  ]
}

# Método 2: Asignar un rol adicional
POST /users/33333333-3333-3333-3333-333333333333/roles/r2222222-2222-2222-2222-222222222222

# Método 3: Remover un rol específico
DELETE /users/33333333-3333-3333-3333-333333333333/roles/r3333333-3333-3333-3333-333333333333
```

### 5. Login y Roles en la Respuesta

```bash
POST /auth/login
{
  "email": "admin@demo.com",
  "password": "password123"
}
```

**Respuesta:**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "userId": "11111111-1111-1111-1111-111111111111",
    "email": "admin@demo.com",
    "roles": ["ADMIN"],  // ⭐ Ahora incluye los códigos de roles
    "tenantId": "a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d"
  }
}
```

---

## 🔒 Control de Acceso (RBAC)

### Permisos por Endpoint

| Endpoint | Roles Permitidos |
|----------|------------------|
| `GET /users` | ADMIN, SUPERVISOR |
| `POST /users` | ADMIN, SUPERVISOR |
| `PUT /users/{id}` | ADMIN, SUPERVISOR |
| `DELETE /users/{id}` | ADMIN |
| `POST /roles` | ADMIN |
| `PUT /roles/{id}` | ADMIN |
| `DELETE /roles/{id}` | ADMIN |
| `PATCH /users/{id}/enable` | ADMIN |
| `PATCH /users/{id}/disable` | ADMIN |

### Validaciones de Seguridad

✅ **Los usuarios solo pueden:**
- Ver/gestionar usuarios de su propio tenant
- Asignar roles que pertenecen a su tenant
- Los roles del sistema (`isSystem=true`) no se pueden eliminar

✅ **Las contraseñas:**
- Mínimo 6 caracteres
- Se encriptan con BCrypt
- No se devuelven en las respuestas

✅ **Multi-tenancy:**
- Todos los endpoints validan el `X-Tenant-Id`
- Aislamiento completo por tenant

---

## 📊 Modelo de Datos

### Tabla: `roles`
```sql
CREATE TABLE roles (
  id UUID PRIMARY KEY,
  tenant_id UUID NOT NULL,
  name VARCHAR(255) NOT NULL,
  code VARCHAR(255) UNIQUE NOT NULL,
  description TEXT,
  permissions TEXT,  -- JSON array
  active BOOLEAN DEFAULT true,
  is_system BOOLEAN DEFAULT false,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
```

### Tabla: `users`
```sql
CREATE TABLE users (
  id UUID PRIMARY KEY,
  tenant_id UUID NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  enabled BOOLEAN DEFAULT true,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
```

### Tabla: `user_roles` (Many-to-Many)
```sql
CREATE TABLE user_roles (
  id UUID PRIMARY KEY,
  user_id UUID REFERENCES users(id),
  role_id UUID REFERENCES roles(id),
  assigned_at TIMESTAMP,
  assigned_by UUID
);
```

---

## 🎨 Estructura de Permisos

Los permisos siguen el formato: `recurso.accion`

### Ejemplos de Permisos:
```json
[
  "*",                    // Todos los permisos
  "leads.*",              // Todos sobre leads
  "leads.read",           // Solo lectura de leads
  "leads.create",         // Crear leads
  "leads.update",         // Actualizar leads
  "leads.delete",         // Eliminar leads
  "contacts.read",        // Lectura de contactos
  "users.read",           // Lectura de usuarios
  "users.create",         // Crear usuarios
  "reports.read",         // Acceso a reportes
  "custom-fields.create"  // Crear campos personalizados
]
```

---

## ✨ Características Implementadas

✅ CRUD completo de Roles con permisos personalizados
✅ CRUD completo de Usuarios con múltiples roles
✅ Roles del sistema (predefinidos e ineliminables)
✅ Soft delete para usuarios y roles
✅ Encriptación BCrypt de contraseñas
✅ Validación de emails únicos
✅ Multi-tenancy con aislamiento completo
✅ Paginación y ordenamiento de usuarios
✅ Asignación/remoción dinámica de roles
✅ Habilitar/deshabilitar usuarios
✅ Auditoría automática (createdAt, updatedAt)
✅ Control de acceso basado en roles (RBAC)
✅ Respuesta de login con roles del usuario

---

## 🚀 Para Probar

1. **Reinicia la aplicación:**
```bash
mvn clean install
mvn spring-boot:run
```

2. **Accede a Swagger UI:**
```
http://localhost:8080/swagger-ui.html
```

3. **Verás dos nuevas secciones:**
   - **Roles** - Gestión de roles
   - **Users** - Gestión de usuarios

4. **Haz login:**
```bash
POST /auth/login
{
  "email": "admin@demo.com",
  "password": "password123"
}
```

5. **Usa el token en Swagger:**
   - Click en "Authorize"
   - Pega el token
   - Agrega el header `X-Tenant-Id: a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d`

---

## 📝 Datos de Prueba Precargados

### Roles:
- **ADMIN** (`r1111111-...`) - Administrador
- **SUPERVISOR** (`r2222222-...`) - Supervisor
- **AGENT** (`r3333333-...`) - Agente
- **USER** (`r4444444-...`) - Usuario

### Usuarios:
- **admin@demo.com** / password123 → Rol: ADMIN
- **supervisor@demo.com** / password123 → Rol: SUPERVISOR
- **agent@demo.com** / password123 → Rol: AGENT

---

¡El sistema de gestión de usuarios y roles está completamente funcional! 🎉

