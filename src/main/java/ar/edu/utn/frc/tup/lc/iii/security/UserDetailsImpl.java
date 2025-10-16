package ar.edu.utn.frc.tup.lc.iii.security;

import ar.edu.utn.frc.tup.lc.iii.directory.entities.User;
import ar.edu.utn.frc.tup.lc.iii.directory.entities.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retornar todos los roles de todos los tenants
        // En el contexto de request, se filtrará por tenant actual
        return user.getUserRoles().stream()
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getCode()))
                .collect(Collectors.toSet());
    }

    // Obtener autoridades para un tenant específico
    public Collection<? extends GrantedAuthority> getAuthoritiesForTenant(UUID tenantId) {
        if (user.isSuperAdmin()) {
            return Set.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
        }

        return user.getUserRoles().stream()
                .filter(userRole -> userRole.getTenantId().equals(tenantId))
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getCode()))
                .collect(Collectors.toSet());
    }

    // Verificar si tiene un rol específico en un tenant
    public boolean hasRoleInTenant(String roleCode, UUID tenantId) {
        if (user.isSuperAdmin()) return true;

        return user.getUserRoles().stream()
                .anyMatch(ur -> ur.getTenantId().equals(tenantId)
                        && ur.getRole().getCode().equals(roleCode));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public User getUser() {
        return user;
    }
}
