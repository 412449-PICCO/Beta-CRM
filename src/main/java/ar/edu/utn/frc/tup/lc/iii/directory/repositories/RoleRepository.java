package ar.edu.utn.frc.tup.lc.iii.directory.repositories;

import ar.edu.utn.frc.tup.lc.iii.directory.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    List<Role> findByTenantIdAndActiveTrue(UUID tenantId);
    Optional<Role> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<Role> findByCodeAndTenantId(String code, UUID tenantId);
    boolean existsByCodeAndTenantId(String code, UUID tenantId);
    List<Role> findByIsSystemTrue();
}

