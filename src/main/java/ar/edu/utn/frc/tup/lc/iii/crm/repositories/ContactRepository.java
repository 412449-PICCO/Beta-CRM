package ar.edu.utn.frc.tup.lc.iii.crm.repositories;

import ar.edu.utn.frc.tup.lc.iii.crm.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
    Page<Contact> findByTenantId(UUID tenantId, Pageable pageable);
    List<Contact> findByTenantId(UUID tenantId);
    Optional<Contact> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<Contact> findByPhoneAndTenantId(String phone, UUID tenantId);
    Optional<Contact> findByEmailAndTenantId(String email, UUID tenantId);
}

