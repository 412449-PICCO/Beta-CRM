package ar.edu.utn.frc.tup.lc.iii.directory.repositories;

import ar.edu.utn.frc.tup.lc.iii.directory.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    List<UserRole> findByUserId(UUID userId);
    void deleteByUserIdAndRoleId(UUID userId, UUID roleId);
    void deleteByUserId(UUID userId);
    Optional<UserRole> findByUserIdAndRoleId(UUID userId, UUID roleId);
}

