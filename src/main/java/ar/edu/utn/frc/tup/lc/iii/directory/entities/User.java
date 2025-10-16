package ar.edu.utn.frc.tup.lc.iii.directory.entities;

import ar.edu.utn.frc.tup.lc.iii.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_email", columnList = "email")
})
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean isSuperAdmin = false; // Super admin con acceso a todos los tenants

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();

    // Helper method: obtener tenants a los que tiene acceso
    public Set<UUID> getAccessibleTenantIds() {
        Set<UUID> tenantIds = new HashSet<>();
        if (userRoles != null) {
            userRoles.forEach(ur -> tenantIds.add(ur.getTenantId()));
        }
        return tenantIds;
    }

    // Helper method: verificar si tiene acceso a un tenant específico
    public boolean hasAccessToTenant(UUID tenantId) {
        if (isSuperAdmin) return true;
        return userRoles != null && userRoles.stream()
                .anyMatch(ur -> ur.getTenantId().equals(tenantId));
    }
}
