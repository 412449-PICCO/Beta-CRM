package ar.edu.utn.frc.tup.lc.iii.omnichannel.repositories;

import ar.edu.utn.frc.tup.lc.iii.omnichannel.entities.Interaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, UUID> {
    Page<Interaction> findByTenantId(UUID tenantId, Pageable pageable);
    List<Interaction> findByTenantIdAndState(UUID tenantId, String state);
    Optional<Interaction> findByIdAndTenantId(UUID id, UUID tenantId);
    List<Interaction> findByTenantIdAndAgentId(UUID tenantId, UUID agentId);
    List<Interaction> findByTenantIdAndLeadId(UUID tenantId, UUID leadId);
}

