# Arquitectura Multi-Tenant del Beta CRM

## Concepto General

El Beta CRM implementa una arquitectura **multi-tenant verdadera** donde:

- Cada **Tenant** = Una cuenta/CRM independiente
- Los **usuarios** son globales y pueden tener acceso a múltiples CRMs
- Cada usuario puede tener **diferentes roles en diferentes CRMs**
- Solo los **Super Admins** tienen acceso completo a todos los tenants

## Modelo de Datos

### 1. Tenants (Cuentas/CRMs)
```
tenants:
  - id: UUID
  - name: String (ej: "Demo Corp CRM")
  - active: Boolean
  - settings: JSON
```

Cada tenant representa un CRM completamente aislado con sus propios:
- Leads
- Contactos
- Interacciones
- Roles personalizados

### 2. Usuarios (Globales)
```
users:
  - id: UUID
  - email: String (único globalmente)
  - password: String (BCrypt)
  - firstName: String
  - lastName: String
  - enabled: Boolean
  - isSuperAdmin: Boolean
```

**Características importantes:**
- Un usuario puede tener acceso a múltiples tenants
- Un usuario puede tener diferentes roles en cada tenant
- `isSuperAdmin = true` → Acceso total a todos los tenants

### 3. Roles (Por Tenant)
```
roles:
  - id: UUID
  - tenantId: UUID (el CRM al que pertenece)
  - name: String
  - code: String (ADMIN, SUPERVISOR, AGENT, etc.)
  - permissions: JSON array
  - isSystem: Boolean
```

Cada tenant tiene sus propios roles. Ejemplos:
- **Demo Corp CRM**: Roles específicos para ese CRM
- **Tech Solutions CRM**: Roles diferentes para ese otro CRM

### 4. UserRoles (Relación Usuario-Rol-Tenant)
```
user_roles:
  - id: UUID
  - userId: UUID
  - roleId: UUID
  - tenantId: UUID ← CLAVE: define en qué CRM aplica este rol
  - assignedAt: Timestamp
  - assignedBy: UUID
```

**Esta es la tabla clave** que permite:
- Un usuario puede ser ADMIN en "Demo Corp CRM"
- El mismo usuario puede ser AGENT en "Tech Solutions CRM"
- Otro usuario puede no tener acceso a ninguno de estos CRMs

## Ejemplos de Escenarios

### Escenario 1: Super Admin
```
Usuario: superadmin@betacrm.com
isSuperAdmin: true
Acceso: TODOS los CRMs automáticamente
Rol efectivo: ADMIN en todos los tenants
```

### Escenario 2: Admin de un solo CRM
```
Usuario: admin@democorp.com
isSuperAdmin: false
UserRoles:
  - tenantId: Demo Corp CRM
    roleId: ADMIN
    
Acceso: Solo "Demo Corp CRM"
Puede: Gestionar ese CRM completamente, dar acceso a otros usuarios
```

### Escenario 3: Usuario Multi-CRM con diferentes roles
```
Usuario: supervisor@multi.com
isSuperAdmin: false
UserRoles:
  - tenantId: Demo Corp CRM
    roleId: SUPERVISOR
  - tenantId: Tech Solutions CRM
    roleId: AGENT
    
Acceso: Dos CRMs con roles diferentes
- En Demo Corp: Puede gestionar equipos y reportes
- En Tech Solutions: Solo operación de leads
```

### Escenario 4: Usuario sin acceso
```
Usuario: agent@democorp.com
isSuperAdmin: false
UserRoles:
  - tenantId: Demo Corp CRM
    roleId: AGENT
    
Acceso: Solo "Demo Corp CRM"
NO puede ver: "Tech Solutions CRM", "Marketing Agency CRM"
```

## Flujo de Autenticación

1. **Login**: Usuario se autentica con email/password
2. **Token JWT**: Se genera incluyendo:
   - User ID
   - Email
   - Lista de tenants accesibles
   - Roles por tenant
   
3. **Selección de Tenant**: 
   - El frontend debe permitir al usuario seleccionar en qué CRM trabajar
   - El header `X-Tenant-Id` debe enviarse en cada request
   
4. **Validación**: 
   - El `TenantFilter` verifica que el usuario tenga acceso al tenant solicitado
   - Se cargan los roles específicos para ese tenant

## Permisos por Tenant

### ADMIN (en un tenant específico)
- Acceso completo al CRM
- Puede invitar usuarios a ese CRM
- Puede asignar roles a otros usuarios en ese CRM
- Puede gestionar configuración del tenant
- **NO puede** dar acceso a otros CRMs (solo Super Admin puede)

### SUPERVISOR
- Gestión de equipos dentro del CRM
- Lectura/escritura de leads y contactos
- Acceso a reportes

### AGENT
- Operación diaria de leads
- Gestión de interacciones
- Lectura de contactos

## Datos de Prueba Incluidos

### Tenants
1. **Demo Corp CRM** (a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d)
2. **Tech Solutions CRM** (b1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d)
3. **Marketing Agency CRM** (c1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d)

### Usuarios (password para todos: `password123`)
1. **superadmin@betacrm.com** - Super Admin (acceso a todos)
2. **admin@democorp.com** - ADMIN solo en Demo Corp
3. **supervisor@multi.com** - SUPERVISOR en Demo Corp, AGENT en Tech Solutions
4. **agent@democorp.com** - AGENT solo en Demo Corp
5. **admin@techsolutions.com** - ADMIN solo en Tech Solutions

## API Endpoints Recomendados

### Gestión de Accesos (Solo ADMIN del tenant)
```
POST /api/tenants/{tenantId}/users/{userId}/roles
  - Asignar un rol a un usuario en este tenant
  
DELETE /api/tenants/{tenantId}/users/{userId}/roles/{roleId}
  - Remover acceso de un usuario a este tenant
  
GET /api/tenants/{tenantId}/users
  - Listar usuarios con acceso a este tenant
```

### Gestión Multi-Tenant (Solo Super Admin)
```
POST /api/tenants
  - Crear un nuevo CRM/Tenant
  
GET /api/tenants
  - Listar todos los tenants
  
POST /api/users/{userId}/super-admin
  - Promover usuario a Super Admin
```

## Consideraciones de Seguridad

1. **Aislamiento de Datos**: Todos los queries deben filtrar por `tenantId`
2. **Validación de Acceso**: Verificar siempre que el usuario tenga acceso al tenant
3. **Roles por Contexto**: Los permisos se evalúan según el tenant activo
4. **Super Admin**: Usar con precaución, tiene acceso irrestricto

## Próximos Pasos de Implementación

- [ ] Actualizar `UserServiceImpl` para manejar usuarios globales
- [ ] Actualizar `AuthController` para devolver lista de tenants accesibles
- [ ] Crear endpoints para gestión de accesos por tenant
- [ ] Implementar selector de tenant en el frontend
- [ ] Agregar validación de permisos por tenant en los servicios

