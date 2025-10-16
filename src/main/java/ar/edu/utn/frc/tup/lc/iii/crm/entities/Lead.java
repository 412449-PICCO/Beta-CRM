package ar.edu.utn.frc.tup.lc.iii.crm.entities;

import ar.edu.utn.frc.tup.lc.iii.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "leads", indexes = {
    @Index(name = "idx_lead_tenant", columnList = "tenant_id"),
    @Index(name = "idx_lead_status", columnList = "status"),
    @Index(name = "idx_lead_phone", columnList = "phone")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lead extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    private String phone;

    private String email;

    private String source;

    @Column(nullable = false)
    private String status = "new"; // new, contacted, qualified, closed

    private Integer score;

    private UUID ownerId; // User que maneja este lead

    @Column(columnDefinition = "TEXT")
    private String notes;
}

