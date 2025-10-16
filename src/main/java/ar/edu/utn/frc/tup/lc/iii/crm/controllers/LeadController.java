package ar.edu.utn.frc.tup.lc.iii.crm.controllers;

import ar.edu.utn.frc.tup.lc.iii.common.ApiResponse;
import ar.edu.utn.frc.tup.lc.iii.crm.dtos.LeadDto;
import ar.edu.utn.frc.tup.lc.iii.crm.services.LeadService;
import ar.edu.utn.frc.tup.lc.iii.multitenancy.TenantContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LeadDto>>> getAllLeads(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<LeadDto> leads = leadService.getAllLeads(tenantId, pageable);
        return ResponseEntity.ok(ApiResponse.success(leads));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeadDto>> getLeadById(@PathVariable UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        LeadDto lead = leadService.getLeadById(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(lead));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<LeadDto>>> getLeadsByStatus(@PathVariable String status) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        List<LeadDto> leads = leadService.getLeadsByStatus(tenantId, status);
        return ResponseEntity.ok(ApiResponse.success(leads));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LeadDto>> createLead(@Valid @RequestBody LeadDto leadDto) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        LeadDto createdLead = leadService.createLead(leadDto, tenantId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lead creado exitosamente", createdLead));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LeadDto>> updateLead(
            @PathVariable UUID id,
            @Valid @RequestBody LeadDto leadDto
    ) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        LeadDto updatedLead = leadService.updateLead(id, leadDto, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Lead actualizado exitosamente", updatedLead));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLead(@PathVariable UUID id) {
        UUID tenantId = UUID.fromString(TenantContext.getTenantId());
        leadService.deleteLead(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success("Lead eliminado exitosamente", null));
    }
}

