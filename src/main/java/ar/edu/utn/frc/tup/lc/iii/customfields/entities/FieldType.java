package ar.edu.utn.frc.tup.lc.iii.customfields.entities;

public enum FieldType {
    TEXT,           // Texto corto
    TEXTAREA,       // Texto largo
    NUMBER,         // Numérico
    DECIMAL,        // Decimal
    DATE,           // Fecha
    DATETIME,       // Fecha y hora
    BOOLEAN,        // Sí/No
    EMAIL,          // Email (con validación)
    PHONE,          // Teléfono
    URL,            // URL
    SELECT,         // Lista de opciones (single)
    MULTISELECT,    // Lista de opciones (multiple)
    CURRENCY        // Moneda
}

