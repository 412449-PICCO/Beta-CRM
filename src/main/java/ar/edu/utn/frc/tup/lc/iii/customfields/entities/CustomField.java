package ar.edu.utn.frc.tup.lc.iii.customfields.entities;

import ar.edu.utn.frc.tup.lc.iii.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "custom_fields", indexes = {
    @Index(name = "idx_custom_field_tenant", columnList = "tenant_id"),
    @Index(name = "idx_custom_field_entity", columnList = "entity_type")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomField extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name; // Nombre para mostrar, ej: "Presupuesto Anual"

    @Column(nullable = false, unique = true)
    private String fieldKey; // Clave única interna, ej: "presupuesto_anual"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FieldType fieldType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityType entityType; // A qué entidad pertenece (CONTACT, LEAD, etc)

    private boolean required = false;

    private String defaultValue;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String options; // JSON con opciones para SELECT/MULTISELECT, ej: ["Opción 1", "Opción 2"]

    @Column(columnDefinition = "TEXT")
    private String validationRules; // JSON con reglas de validación

    private Integer displayOrder = 0; // Orden de visualización

    private boolean active = true;
}

