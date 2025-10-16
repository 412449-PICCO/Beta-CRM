package ar.edu.utn.frc.tup.lc.iii.directory.services;

import ar.edu.utn.frc.tup.lc.iii.directory.dtos.CreateTenantRequest;
import ar.edu.utn.frc.tup.lc.iii.directory.dtos.TenantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TenantService {

    /**
     * Crea un nuevo CRM (tenant)
     */
    TenantDto createTenant(CreateTenantRequest request);

    /**
     * Obtiene un CRM por ID
     */
    TenantDto getTenantById(UUID id);

    /**
     * Obtiene todos los CRMs activos
     */
    List<TenantDto> getAllActiveTenants();

    /**
     * Obtiene todos los CRMs con paginación
     */
    Page<TenantDto> getAllTenants(Pageable pageable);

    /**
     * Actualiza un CRM
     */
    TenantDto updateTenant(UUID id, CreateTenantRequest request);

    /**
     * Desactiva un CRM (soft delete)
     */
    void deactivateTenant(UUID id);

    /**
     * Activa un CRM
     */
    void activateTenant(UUID id);
}

