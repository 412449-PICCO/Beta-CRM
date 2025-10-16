package ar.edu.utn.frc.tup.lc.iii.repositories;


import ar.edu.utn.frc.tup.lc.iii.entities.DummyEntity;
import ar.edu.utn.frc.tup.lc.iii.models.Dummy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyRepository extends JpaRepository<DummyEntity, Long> {


}
