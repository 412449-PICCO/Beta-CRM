# 🔐 Credenciales de Prueba

Este archivo contiene las credenciales predefinidas para pruebas del sistema.

## Tenant de Prueba

**Tenant ID**: `a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d`
**Nombre**: Demo Tenant

## Usuarios de Prueba

Todos los usuarios usan la contraseña: `password123`

### Administrador
- **Email**: `admin@demo.com`
- **Password**: `password123`
- **Role**: ADMIN
- **User ID**: `11111111-1111-1111-1111-111111111111`

### Supervisor
- **Email**: `supervisor@demo.com`
- **Password**: `password123`
- **Role**: SUPERVISOR
- **User ID**: `22222222-2222-2222-2222-222222222222`

### Agente
- **Email**: `agent@demo.com`
- **Password**: `password123`
- **Role**: AGENT
- **User ID**: `33333333-3333-3333-3333-333333333333`

## Ejemplo de Request de Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@demo.com",
    "password": "password123"
  }'
```

## Ejemplo de Request con Auth

```bash
# Primero hacer login y copiar el token
TOKEN="<tu-token-aqui>"

# Luego usar el token en las peticiones
curl -X GET http://localhost:8080/leads \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-Tenant-Id: a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d"
```

## IDs de Datos de Prueba

### Leads
- `aaaaaaaa-1111-1111-1111-111111111111` - Juan Pérez (new)
- `bbbbbbbb-2222-2222-2222-222222222222` - María González (contacted)
- `cccccccc-3333-3333-3333-333333333333` - Carlos Ruiz (qualified)
- `dddddddd-4444-4444-4444-444444444444` - Ana Martínez (new, sin asignar)

### Contactos
- `eeeeeeee-1111-1111-1111-111111111111` - Roberto Sánchez (Tech Corp)
- `ffffffff-2222-2222-2222-222222222222` - Laura Hernández (Marketing Inc)

### Interacciones
- `aaaabbbb-1111-1111-1111-111111111111` - Llamada de voz con Juan Pérez
- `bbbbcccc-2222-2222-2222-222222222222` - WhatsApp con María González
- `ccccdddd-3333-3333-3333-333333333333` - Email con Carlos Ruiz

## Datos de Prueba Precargados

### Leads (4 registros)
- Juan Pérez - nuevo lead
- María González - contactado
- Carlos Ruiz - calificado
- Ana Martínez - nuevo sin asignar

### Contactos (2 registros)
- Roberto Sánchez - Tech Corp
- Laura Hernández - Marketing Inc

### Interacciones (3 registros)
- Llamada de voz con Juan Pérez
- WhatsApp con María González
- Email con Carlos Ruiz

## ⚠️ Importante

**NO uses estas credenciales en producción**. Este archivo es solo para desarrollo y pruebas locales.
