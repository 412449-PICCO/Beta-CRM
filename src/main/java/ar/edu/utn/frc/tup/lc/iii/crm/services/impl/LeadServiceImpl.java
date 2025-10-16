package ar.edu.utn.frc.tup.lc.iii.crm.services.impl;

import ar.edu.utn.frc.tup.lc.iii.crm.dtos.LeadDto;
import ar.edu.utn.frc.tup.lc.iii.crm.entities.Lead;
import ar.edu.utn.frc.tup.lc.iii.crm.repositories.LeadRepository;
import ar.edu.utn.frc.tup.lc.iii.crm.services.LeadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public LeadDto createLead(LeadDto leadDto, UUID tenantId) {
        Lead lead = modelMapper.map(leadDto, Lead.class);
        lead.setTenantId(tenantId);
        lead.setId(null); // Asegurar que sea nuevo

        if (lead.getStatus() == null || lead.getStatus().isEmpty()) {
            lead.setStatus("new");
        }

        Lead savedLead = leadRepository.save(lead);
        return modelMapper.map(savedLead, LeadDto.class);
    }

    @Override
    @Transactional
    public LeadDto updateLead(UUID id, LeadDto leadDto, UUID tenantId) {
        Lead lead = leadRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Lead no encontrado"));

        // Actualizar solo campos permitidos
        if (leadDto.getName() != null) lead.setName(leadDto.getName());
        if (leadDto.getPhone() != null) lead.setPhone(leadDto.getPhone());
        if (leadDto.getEmail() != null) lead.setEmail(leadDto.getEmail());
        if (leadDto.getSource() != null) lead.setSource(leadDto.getSource());
        if (leadDto.getStatus() != null) lead.setStatus(leadDto.getStatus());
        if (leadDto.getScore() != null) lead.setScore(leadDto.getScore());
        if (leadDto.getOwnerId() != null) lead.setOwnerId(leadDto.getOwnerId());
        if (leadDto.getNotes() != null) lead.setNotes(leadDto.getNotes());

        Lead updatedLead = leadRepository.save(lead);
        return modelMapper.map(updatedLead, LeadDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadDto getLeadById(UUID id, UUID tenantId) {
        Lead lead = leadRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Lead no encontrado"));
        return modelMapper.map(lead, LeadDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeadDto> getAllLeads(UUID tenantId, Pageable pageable) {
        return leadRepository.findByTenantId(tenantId, pageable)
                .map(lead -> modelMapper.map(lead, LeadDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadDto> getLeadsByStatus(UUID tenantId, String status) {
        return leadRepository.findByTenantIdAndStatus(tenantId, status)
                .stream()
                .map(lead -> modelMapper.map(lead, LeadDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteLead(UUID id, UUID tenantId) {
        Lead lead = leadRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Lead no encontrado"));
        leadRepository.delete(lead);
    }
}

