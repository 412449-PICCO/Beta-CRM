package ar.edu.utn.frc.tup.lc.iii.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private UUID userId;
    private String email;
    private boolean isSuperAdmin;
    private Set<UUID> accessibleTenants; // IDs de los CRMs a los que tiene acceso
    private Map<UUID, Set<String>> tenantRoles; // Mapa de tenantId -> roles en ese tenant
}
