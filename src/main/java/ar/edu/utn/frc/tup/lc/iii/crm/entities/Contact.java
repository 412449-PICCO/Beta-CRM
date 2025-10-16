package ar.edu.utn.frc.tup.lc.iii.crm.entities;

import ar.edu.utn.frc.tup.lc.iii.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "contacts", indexes = {
    @Index(name = "idx_contact_tenant", columnList = "tenant_id"),
    @Index(name = "idx_contact_phone", columnList = "phone"),
    @Index(name = "idx_contact_email", columnList = "email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String company;

    private String position;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String extraData; // JSON con campos personalizados
}

