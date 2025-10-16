package ar.edu.utn.frc.tup.lc.iii.crm.repositories;

import ar.edu.utn.frc.tup.lc.iii.crm.entities.Lead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadRepository extends JpaRepository<Lead, UUID> {
    Page<Lead> findByTenantId(UUID tenantId, Pageable pageable);
    List<Lead> findByTenantId(UUID tenantId);
    Optional<Lead> findByIdAndTenantId(UUID id, UUID tenantId);
    List<Lead> findByTenantIdAndStatus(UUID tenantId, String status);
    List<Lead> findByTenantIdAndOwnerId(UUID tenantId, UUID ownerId);
}

