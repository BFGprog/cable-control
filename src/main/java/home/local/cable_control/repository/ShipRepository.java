package home.local.cable_control.repository;

import home.local.cable_control.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {

    boolean existsByNameAndNumAndProjectNumAndProjectName(
            String name,
            String num,
            String projectNum,
            String projectName
    );
    List<Ship> findAllByOrderByNumAscIdAsc();
}
