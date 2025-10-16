package ar.edu.utn.frc.tup.lc.iii.crm.services;

import ar.edu.utn.frc.tup.lc.iii.crm.dtos.LeadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LeadService {
    LeadDto createLead(LeadDto leadDto, UUID tenantId);
    LeadDto updateLead(UUID id, LeadDto leadDto, UUID tenantId);
    LeadDto getLeadById(UUID id, UUID tenantId);
    Page<LeadDto> getAllLeads(UUID tenantId, Pageable pageable);
    List<LeadDto> getLeadsByStatus(UUID tenantId, String status);
    void deleteLead(UUID id, UUID tenantId);
}

