package home.local.cable_control.repository;

import home.local.cable_control.model.CableJournal;
import home.local.cable_control.model.CableSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CableScheduleRepository extends JpaRepository<CableSchedule, Long> {


    void deleteByCableJournal(CableJournal cableJournal);

}
