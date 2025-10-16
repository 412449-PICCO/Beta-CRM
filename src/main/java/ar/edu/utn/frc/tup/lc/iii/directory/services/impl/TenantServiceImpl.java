package ar.edu.utn.frc.tup.lc.iii.directory.services.impl;

import ar.edu.utn.frc.tup.lc.iii.directory.dtos.CreateTenantRequest;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.TenantDto;
import ar.edu.utn.frc.tup.lc.iii.directory.entities.Tenant;
import ar.edu.utn.frc.tup.lc.iii.directory.repositories.TenantRepository;
import ar.edu.utn.frc.tup.lc.iii.directory.services.TenantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public TenantDto createTenant(CreateTenantRequest request) {
        // Validar que no exista un CRM con el mismo nombre
        if (tenantRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Ya existe un CRM con el nombre: " + request.getName());
        }

        Tenant tenant = Tenant.builder()
                .name(request.getName())
                .active(true)
                .build();

        // Convertir settings a JSON si existen
        if (request.getSettings() != null && !request.getSettings().isEmpty()) {
            try {
                tenant.setSettings(objectMapper.writeValueAsString(request.getSettings()));
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error al procesar la configuración del CRM");
            }
        }

        Tenant savedTenant = tenantRepository.save(tenant);
        return mapToDto(savedTenant);
    }

    @Override
    @Transactional(readOnly = true)
    public TenantDto getTenantById(UUID id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CRM no encontrado con ID: " + id));
        return mapToDto(tenant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantDto> getAllActiveTenants() {
        return tenantRepository.findByActiveTrue()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TenantDto> getAllTenants(Pageable pageable) {
        return tenantRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional
    public TenantDto updateTenant(UUID id, CreateTenantRequest request) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CRM no encontrado con ID: " + id));

        // Validar que no exista otro CRM con el mismo nombre
        if (!tenant.getName().equals(request.getName()) &&
            tenantRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Ya existe un CRM con el nombre: " + request.getName());
        }

        tenant.setName(request.getName());

        // Actualizar settings
        if (request.getSettings() != null) {
            try {
                tenant.setSettings(objectMapper.writeValueAsString(request.getSettings()));
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error al procesar la configuración del CRM");
            }
        }

        Tenant updatedTenant = tenantRepository.save(tenant);
        return mapToDto(updatedTenant);
    }

    @Override
    @Transactional
    public void deactivateTenant(UUID id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CRM no encontrado con ID: " + id));

        tenant.setActive(false);
        tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public void activateTenant(UUID id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CRM no encontrado con ID: " + id));

        tenant.setActive(true);
        tenantRepository.save(tenant);
    }

    private TenantDto mapToDto(Tenant tenant) {
        TenantDto dto = TenantDto.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .active(tenant.isActive())
                .createdAt(tenant.getCreatedAt())
                .build();

        // Deserializar settings si existen
        if (tenant.getSettings() != null && !tenant.getSettings().isEmpty()) {
            try {
                Map<String, Object> settings = objectMapper.readValue(
                        tenant.getSettings(),
                        new TypeReference<Map<String, Object>>() {}
                );
                dto.setSettings(settings);
            } catch (JsonProcessingException e) {
                dto.setSettings(new HashMap<>());
            }
        }

        return dto;
    }
}

