package home.local.cable_control.repository;

import home.local.cable_control.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {

    boolean existsByNameAndNumAndProjectNumAndProjectName(
            String name,
            String num,
            String projectNum,
            String projectName
    );
}
