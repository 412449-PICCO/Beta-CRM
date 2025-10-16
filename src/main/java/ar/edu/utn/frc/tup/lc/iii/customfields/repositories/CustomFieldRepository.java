package ar.edu.utn.frc.tup.lc.iii.customfields.repositories;

import ar.edu.utn.frc.tup.lc.iii.customfields.entities.CustomField;
import ar.edu.utn.frc.tup.lc.iii.customfields.entities.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomFieldRepository extends JpaRepository<CustomField, UUID> {
    List<CustomField> findByTenantIdAndEntityTypeAndActiveTrue(UUID tenantId, EntityType entityType);
    List<CustomField> findByTenantIdAndActiveTrue(UUID tenantId);
    Optional<CustomField> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<CustomField> findByFieldKeyAndTenantId(String fieldKey, UUID tenantId);
    boolean existsByFieldKeyAndTenantId(String fieldKey, UUID tenantId);
}

