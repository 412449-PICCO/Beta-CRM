package ar.edu.utn.frc.tup.lc.iii.customfields.entities;

import ar.edu.utn.frc.tup.lc.iii.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "custom_field_values", indexes = {
    @Index(name = "idx_cfv_field", columnList = "field_id"),
    @Index(name = "idx_cfv_entity", columnList = "entity_id"),
    @Index(name = "idx_cfv_tenant", columnList = "tenant_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomFieldValue extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID fieldId; // Referencia al CustomField

    @Column(nullable = false)
    private UUID entityId; // ID de la entidad (Contact, Lead, etc)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityType entityType;

    @Column(name = "field_value", columnDefinition = "TEXT")
    private String fieldValue; // Valor almacenado como texto, se convierte según el tipo
}
