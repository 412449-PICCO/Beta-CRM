package ar.edu.utn.frc.tup.lc.iii.directory.entities;

import ar.edu.utn.frc.tup.lc.iii.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles",
    indexes = {
        @Index(name = "idx_role_tenant", columnList = "tenant_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_role_code_tenant", columnNames = {"code", "tenant_id"})
    }
)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code; // Código único por tenant, ej: "ADMIN"

    private String description;

    @Column(columnDefinition = "TEXT")
    private String permissions; // JSON con array de permisos

    private boolean active = true;

    private boolean isSystem = false; // true para roles predefinidos del sistema

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();
}
