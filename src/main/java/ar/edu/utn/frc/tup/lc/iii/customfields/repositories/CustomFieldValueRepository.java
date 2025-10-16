package ar.edu.utn.frc.tup.lc.iii.customfields.repositories;

import ar.edu.utn.frc.tup.lc.iii.customfields.entities.CustomFieldValue;
import ar.edu.utn.frc.tup.lc.iii.customfields.entities.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomFieldValueRepository extends JpaRepository<CustomFieldValue, UUID> {
    List<CustomFieldValue> findByEntityIdAndEntityType(UUID entityId, EntityType entityType);
    Optional<CustomFieldValue> findByFieldIdAndEntityId(UUID fieldId, UUID entityId);
    void deleteByEntityIdAndEntityType(UUID entityId, EntityType entityType);
    List<CustomFieldValue> findByFieldId(UUID fieldId);
}

