package ar.edu.utn.frc.tup.lc.iii.directory.repositories;

import ar.edu.utn.frc.tup.lc.iii.directory.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndEnabledTrue(String email);

    @Query("SELECT DISTINCT u FROM User u JOIN u.userRoles ur WHERE ur.tenantId = :tenantId")
    List<User> findByTenantId(@Param("tenantId") UUID tenantId);

    @Query("SELECT DISTINCT u FROM User u JOIN u.userRoles ur WHERE ur.tenantId = :tenantId")
    Page<User> findByTenantId(@Param("tenantId") UUID tenantId, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.userRoles ur WHERE u.id = :id AND ur.tenantId = :tenantId")
    Optional<User> findByIdAndTenantId(@Param("id") UUID id, @Param("tenantId") UUID tenantId);
}
