# 🚀 Guía Rápida de Inicio

## Ejecutar el Proyecto

```bash
# Limpiar y compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

## ✅ La aplicación debería iniciar en el puerto 8080

### Puntos de acceso:
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

### Configuración H2 Console:
- **JDBC URL**: `jdbc:h2:mem:betacrm`
- **User**: `sa`
- **Password**: (dejar en blanco)

## 🧪 Probar la API

### 1. Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@demo.com",
    "password": "password123"
  }'
```

**Respuesta esperada:**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "userId": "u1111111-1111-1111-1111-111111111111",
    "email": "admin@demo.com",
    "role": "ADMIN",
    "tenantId": "a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d"
  },
  "timestamp": "2025-10-16T14:30:00.000Z"
}
```

### 2. Obtener Leads (usar el token del login)
```bash
curl -X GET http://localhost:8080/leads \
  -H "Authorization: Bearer <TU-TOKEN-AQUI>" \
  -H "X-Tenant-Id: a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d"
```

## 📊 Datos de Prueba Precargados

El script `data.sql` carga automáticamente:
- ✅ 1 Tenant: "Demo Tenant"
- ✅ 3 Usuarios: admin, supervisor, agent
- ✅ 4 Leads de ejemplo
- ✅ 2 Contactos de ejemplo
- ✅ 3 Interacciones de ejemplo

## 🔐 Credenciales

| Usuario | Email | Password | Role |
|---------|-------|----------|------|
| Admin | admin@demo.com | password123 | ADMIN |
| Supervisor | supervisor@demo.com | password123 | SUPERVISOR |
| Agente | agent@demo.com | password123 | AGENT |

**Tenant ID**: `a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d`

## 📦 Colección de Postman

Importa el archivo `Beta-CRM.postman_collection.json` en Postman para tener todos los endpoints listos para probar.

## ❗ Solución de Problemas

### Error: "Table not found"
- Asegúrate de que `spring.jpa.defer-datasource-initialization=true` esté en `application.properties`

### Error: Puerto 8080 ocupado
- Cambia el puerto en `application.properties`: `server.port=8081`

### Base de datos vacía
- Verifica que `spring.sql.init.mode=always` esté configurado
- Las tablas se crean automáticamente con `spring.jpa.hibernate.ddl-auto=create-drop`

## 🎯 Próximos Pasos

Una vez que la aplicación esté corriendo:

1. ✅ Prueba el login con Swagger UI
2. ✅ Explora los endpoints de Leads
3. ✅ Revisa la base de datos en H2 Console
4. 🚧 Implementar ContactService y Controller (Fase 2)
5. 🚧 Implementar InteractionService (Fase 3)

## 📝 Notas

- La base de datos H2 se reinicia cada vez que se detiene la aplicación (`create-drop`)
- Para persistencia, cambia a `update` en producción o usa PostgreSQL
- Todos los endpoints (excepto `/auth/login`) requieren JWT + header `X-Tenant-Id`

