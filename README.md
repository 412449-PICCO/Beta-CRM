# Beta CRM - Backend Multi-tenant

CRM Multi-tenant con capacidades Omnicanal basado en Spring Boot 3.5.2 + Java 17 + PostgreSQL/H2

## 🏗️ Arquitectura

El proyecto está estructurado siguiendo el blueprint técnico de CRM tipo Neotel:

```
ar.edu.utn.frc.tup.lc.iii
├── common/                    # Clases base y utilidades
│   ├── AuditableEntity.java   # Entidad base con auditoría
│   ├── ApiResponse.java       # Respuesta estándar de API
│   └── GlobalExceptionHandler.java
├── config/                    # Configuraciones
│   ├── MappersConfig.java
│   └── SpringDocConfig.java
├── multitenancy/              # Sistema multi-tenant
│   ├── TenantContext.java
│   └── TenantFilter.java
├── security/                  # JWT + Spring Security
│   ├── SecurityConfig.java
│   ├── JwtService.java
│   ├── JwtAuthFilter.java
│   ├── UserDetailsImpl.java
│   └── UserDetailsServiceImpl.java
├── directory/                 # Usuarios, roles y tenants
│   ├── entities/
│   │   ├── Tenant.java
│   │   ├── User.java
│   │   └── RoleType.java
│   └── repositories/
├── crm/                      # Core del CRM (leads, contactos)
│   ├── entities/
│   │   ├── Lead.java
│   │   └── Contact.java
│   ├── repositories/
│   ├── services/
│   ├── dtos/
│   └── controllers/
└── omnichannel/              # Interacciones multicanal
    ├── entities/
    │   ├── Interaction.java
    │   ├── ChannelType.java
    │   └── Direction.java
    ├── repositories/
    └── dtos/
```

## 🚀 Características

- ✅ **Multi-tenancy** mediante header `X-Tenant-Id`
- ✅ **Autenticación JWT** con Spring Security
- ✅ **CRUD completo de Leads** con paginación y filtros
- ✅ **Gestión de Contactos**
- ✅ **Sistema de Interacciones Omnicanal** (Voz, WhatsApp, Email, SMS, Webchat)
- ✅ **Auditoría automática** de entidades
- ✅ **Documentación OpenAPI/Swagger**
- ✅ **Manejo global de excepciones**
- ✅ **Base de datos H2 para desarrollo**

## 📋 Requisitos

- Java 17 o superior
- Maven 3.6+
- PostgreSQL 12+ (para producción)

## ⚙️ Configuración

### Base de datos

Por defecto, el proyecto usa H2 en memoria para desarrollo. Para usar PostgreSQL:

1. Edita `application.properties`
2. Comenta la configuración de H2
3. Descomenta y configura PostgreSQL

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/betacrm
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### JWT Secret

Cambia el secreto JWT en producción:

```properties
app.jwt.secret=TU_SECRET_AQUI_BASE64
app.jwt.expiration=3600000
```

## 🏃 Ejecutar el proyecto

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## 📚 Documentación API

Una vez iniciada la aplicación, accede a:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **H2 Console**: http://localhost:8080/h2-console (solo desarrollo)

## 🔐 Autenticación

### 1. Login

```bash
POST /auth/login
Content-Type: application/json

{
  "email": "admin@example.com",
  "password": "password123"
}
```

Respuesta:
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "userId": "uuid",
    "email": "admin@example.com",
    "role": "ADMIN",
    "tenantId": "uuid"
  },
  "timestamp": "2025-10-16T12:00:00Z"
}
```

### 2. Usar el token

Incluye en todas las peticiones protegidas:

```
Authorization: Bearer <token>
X-Tenant-Id: <tenant-uuid>
```

## 📝 Ejemplos de uso

### Crear un Lead

```bash
POST /leads
Authorization: Bearer <token>
X-Tenant-Id: <tenant-uuid>
Content-Type: application/json

{
  "name": "Juan Pérez",
  "phone": "+5215512345678",
  "email": "juan@example.com",
  "source": "Facebook Ads",
  "status": "new"
}
```

### Listar Leads con paginación

```bash
GET /leads?page=0&size=10&sortBy=createdAt&sortDir=DESC
Authorization: Bearer <token>
X-Tenant-Id: <tenant-uuid>
```

### Filtrar Leads por estado

```bash
GET /leads/status/new
Authorization: Bearer <token>
X-Tenant-Id: <tenant-uuid>
```

## 🗄️ Modelo de Datos

### Tenant
- Multi-tenancy por fila (row-level)
- Configuración flexible en JSON

### User
- Vinculado a un Tenant
- Roles: ADMIN, SUPERVISOR, AGENT, USER
- Autenticación por email/password

### Lead
- Gestión de prospectos
- Estados: new, contacted, qualified, closed
- Scoring y asignación a agentes

### Interaction
- Interacciones omnicanal
- Canales: VOICE, WHATSAPP, EMAIL, SMS, WEBCHAT
- Dirección: INBOUND, OUTBOUND
- Estados: queued, active, ended

## 🛠️ Próximos pasos

Según el roadmap del blueprint:

- [ ] **Fase 2**: Contact Service completo
- [ ] **Fase 3**: Sistema de colas y routing
- [ ] **Fase 4**: WebSocket para tiempo real
- [ ] **Fase 5**: Integración con Asterisk/FreeSWITCH
- [ ] **Fase 6**: Dialer (preview/power/predictive)
- [ ] **Fase 7**: Mensajería WhatsApp/SMS
- [ ] **Fase 8**: Reporting y métricas

## 📄 Licencia

Proyecto educativo - UTN FRC

## 👨‍💻 Autor

Constantino Picco - costipicco@gmail.com

