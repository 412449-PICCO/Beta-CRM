package ar.edu.utn.frc.tup.lc.iii.directory.repositories;

import ar.edu.utn.frc.tup.lc.iii.directory.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    Optional<Tenant> findByName(String name);
    Optional<Tenant> findByIdAndActiveTrue(UUID id);
    boolean existsByName(String name);
    List<Tenant> findByActiveTrue();
}
