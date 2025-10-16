-- Script de inicialización para datos de prueba
-- Este script se ejecuta automáticamente con H2

-- Insertar tenants de prueba (diferentes CRMs/Cuentas)
INSERT INTO tenants (id, name, active, settings, created_at) VALUES
(CAST('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Demo Corp CRM', true, '{"plan": "enterprise", "features": ["crm", "omnichannel"]}', CURRENT_TIMESTAMP);

INSERT INTO tenants (id, name, active, settings, created_at) VALUES
(CAST('b1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Tech Solutions CRM', true, '{"plan": "professional", "features": ["crm"]}', CURRENT_TIMESTAMP);

INSERT INTO tenants (id, name, active, settings, created_at) VALUES
(CAST('c1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Marketing Agency CRM', true, '{"plan": "basic", "features": ["crm"]}', CURRENT_TIMESTAMP);

-- Insertar roles del sistema para cada tenant
-- Roles para Demo Corp CRM
INSERT INTO roles (id, tenant_id, name, code, description, permissions, active, is_system, created_at) VALUES
(CAST('11111111-1111-1111-1111-111111111111' AS UUID), CAST('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Administrador', 'ADMIN', 'Acceso completo al CRM', '["*"]', true, true, CURRENT_TIMESTAMP);

INSERT INTO roles (id, tenant_id, name, code, description, permissions, active, is_system, created_at) VALUES
(CAST('22222222-2222-2222-2222-222222222222' AS UUID), CAST('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Supervisor', 'SUPERVISOR', 'Gestión de equipos', '["users.read", "leads.*", "contacts.*", "reports.read"]', true, true, CURRENT_TIMESTAMP);

INSERT INTO roles (id, tenant_id, name, code, description, permissions, active, is_system, created_at) VALUES
(CAST('33333333-3333-3333-3333-333333333333' AS UUID), CAST('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Agente', 'AGENT', 'Operación de leads', '["leads.read", "leads.update", "contacts.read", "contacts.update", "interactions.*"]', true, true, CURRENT_TIMESTAMP);

-- Roles para Tech Solutions CRM
INSERT INTO roles (id, tenant_id, name, code, description, permissions, active, is_system, created_at) VALUES
(CAST('44444444-4444-4444-4444-444444444444' AS UUID), CAST('b1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Administrador', 'ADMIN', 'Acceso completo al CRM', '["*"]', true, true, CURRENT_TIMESTAMP);

INSERT INTO roles (id, tenant_id, name, code, description, permissions, active, is_system, created_at) VALUES
(CAST('55555555-5555-5555-5555-555555555555' AS UUID), CAST('b1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Agente', 'AGENT', 'Operación de leads', '["leads.read", "leads.update", "contacts.read"]', true, true, CURRENT_TIMESTAMP);

-- Roles para Marketing Agency CRM
INSERT INTO roles (id, tenant_id, name, code, description, permissions, active, is_system, created_at) VALUES
(CAST('66666666-6666-6666-6666-666666666666' AS UUID), CAST('c1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Administrador', 'ADMIN', 'Acceso completo al CRM', '["*"]', true, true, CURRENT_TIMESTAMP);

-- Insertar usuarios de prueba (GLOBALES - sin tenant específico)
-- Usuario Super Admin (acceso a todos los CRMs)
INSERT INTO users (id, email, password, first_name, last_name, enabled, is_super_admin, created_at) VALUES
(CAST('a1111111-1111-1111-1111-111111111111' AS UUID), 'superadmin@betacrm.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Super', 'Admin', true, true, CURRENT_TIMESTAMP);

-- Usuario Admin de Demo Corp
INSERT INTO users (id, email, password, first_name, last_name, enabled, is_super_admin, created_at) VALUES
(CAST('a2222222-2222-2222-2222-222222222222' AS UUID), 'admin@democorp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'Demo Corp', true, false, CURRENT_TIMESTAMP);

-- Usuario Supervisor con acceso a Demo Corp y Tech Solutions
INSERT INTO users (id, email, password, first_name, last_name, enabled, is_super_admin, created_at) VALUES
(CAST('a3333333-3333-3333-3333-333333333333' AS UUID), 'supervisor@multi.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Multi', 'Supervisor', true, false, CURRENT_TIMESTAMP);

-- Usuario Agente solo en Demo Corp
INSERT INTO users (id, email, password, first_name, last_name, enabled, is_super_admin, created_at) VALUES
(CAST('a4444444-4444-4444-4444-444444444444' AS UUID), 'agent@democorp.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Agente', 'Demo', true, false, CURRENT_TIMESTAMP);

-- Usuario Admin de Tech Solutions
INSERT INTO users (id, email, password, first_name, last_name, enabled, is_super_admin, created_at) VALUES
(CAST('a5555555-5555-5555-5555-555555555555' AS UUID), 'admin@techsolutions.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'Tech Solutions', true, false, CURRENT_TIMESTAMP);

-- Asignar roles a usuarios en diferentes tenants
-- Admin de Demo Corp (solo en Demo Corp)
INSERT INTO user_roles (id, user_id, role_id, tenant_id, assigned_at) VALUES
(CAST('b1111111-1111-1111-1111-111111111111' AS UUID), CAST('a2222222-2222-2222-2222-222222222222' AS UUID), CAST('11111111-1111-1111-1111-111111111111' AS UUID), CAST('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), CURRENT_TIMESTAMP);

-- Supervisor Multi-CRM (Supervisor en Demo Corp, Agente en Tech Solutions)
INSERT INTO user_roles (id, user_id, role_id, tenant_id, assigned_at) VALUES
(CAST('b2222222-2222-2222-2222-222222222222' AS UUID), CAST('a3333333-3333-3333-3333-333333333333' AS UUID), CAST('22222222-2222-2222-2222-222222222222' AS UUID), CAST('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), CURRENT_TIMESTAMP);

INSERT INTO user_roles (id, user_id, role_id, tenant_id, assigned_at) VALUES
(CAST('b3333333-3333-3333-3333-333333333333' AS UUID), CAST('a3333333-3333-3333-3333-333333333333' AS UUID), CAST('55555555-5555-5555-5555-555555555555' AS UUID), CAST('b1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), CURRENT_TIMESTAMP);

-- Agente solo en Demo Corp
INSERT INTO user_roles (id, user_id, role_id, tenant_id, assigned_at) VALUES
(CAST('b4444444-4444-4444-4444-444444444444' AS UUID), CAST('a4444444-4444-4444-4444-444444444444' AS UUID), CAST('33333333-3333-3333-3333-333333333333' AS UUID), CAST('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), CURRENT_TIMESTAMP);

-- Admin de Tech Solutions (solo en Tech Solutions)
INSERT INTO user_roles (id, user_id, role_id, tenant_id, assigned_at) VALUES
(CAST('b5555555-5555-5555-5555-555555555555' AS UUID), CAST('a5555555-5555-5555-5555-555555555555' AS UUID), CAST('44444444-4444-4444-4444-444444444444' AS UUID), CAST('b1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), CURRENT_TIMESTAMP);

-- Insertar leads de prueba en Demo Corp CRM
INSERT INTO leads (id, tenant_id, name, phone, email, source, status, score, owner_id, notes, created_at) VALUES
(CAST('c1111111-1111-1111-1111-111111111111' AS UUID), CAST('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Juan Pérez', '+5215512345678', 'juan.perez@example.com', 'Facebook Ads', 'new', 75, CAST('a4444444-4444-4444-4444-444444444444' AS UUID), 'Lead Demo Corp', CURRENT_TIMESTAMP);

INSERT INTO leads (id, tenant_id, name, phone, email, source, status, score, owner_id, notes, created_at) VALUES
(CAST('c2222222-2222-2222-2222-222222222222' AS UUID), CAST('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'María González', '+5215587654321', 'maria.gonzalez@example.com', 'Google Ads', 'contacted', 60, CAST('a4444444-4444-4444-4444-444444444444' AS UUID), 'Lead Demo Corp', CURRENT_TIMESTAMP);

-- Insertar leads de prueba en Tech Solutions CRM
INSERT INTO leads (id, tenant_id, name, phone, email, source, status, score, owner_id, notes, created_at) VALUES
(CAST('c3333333-3333-3333-3333-333333333333' AS UUID), CAST('b1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Carlos Ruiz', '+5215598765432', 'carlos.ruiz@example.com', 'Website', 'qualified', 85, CAST('a3333333-3333-3333-3333-333333333333' AS UUID), 'Lead Tech Solutions', CURRENT_TIMESTAMP);

-- Insertar contactos de prueba en Demo Corp
INSERT INTO contacts (id, tenant_id, first_name, last_name, phone, email, company, position, notes, created_at) VALUES
(CAST('d1111111-1111-1111-1111-111111111111' AS UUID), CAST('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Roberto', 'Sánchez', '+5215511112222', 'roberto.sanchez@company.com', 'Tech Corp', 'CTO', 'Contacto Demo Corp', CURRENT_TIMESTAMP);

-- Insertar contactos de prueba en Tech Solutions
INSERT INTO contacts (id, tenant_id, first_name, last_name, phone, email, company, position, notes, created_at) VALUES
(CAST('d2222222-2222-2222-2222-222222222222' AS UUID), CAST('b1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'Laura', 'Hernández', '+5215533334444', 'laura.hernandez@company.com', 'Marketing Inc', 'Marketing Manager', 'Contacto Tech Solutions', CURRENT_TIMESTAMP);

-- Insertar interacciones de prueba
INSERT INTO interactions (id, tenant_id, channel, direction, state, agent_id, lead_id, started_at, duration, notes, created_at) VALUES
(CAST('e1111111-1111-1111-1111-111111111111' AS UUID), CAST('a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'VOICE', 'INBOUND', 'ended', CAST('a4444444-4444-4444-4444-444444444444' AS UUID), CAST('c1111111-1111-1111-1111-111111111111' AS UUID), DATEADD('HOUR', -2, CURRENT_TIMESTAMP), 300, 'Llamada Demo Corp', CURRENT_TIMESTAMP);

INSERT INTO interactions (id, tenant_id, channel, direction, state, agent_id, lead_id, started_at, duration, notes, created_at) VALUES
(CAST('e2222222-2222-2222-2222-222222222222' AS UUID), CAST('b1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d' AS UUID), 'WHATSAPP', 'OUTBOUND', 'ended', CAST('a3333333-3333-3333-3333-333333333333' AS UUID), CAST('c3333333-3333-3333-3333-333333333333' AS UUID), DATEADD('HOUR', -1, CURRENT_TIMESTAMP), 120, 'WhatsApp Tech Solutions', CURRENT_TIMESTAMP);
