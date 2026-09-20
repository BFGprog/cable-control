package home.local.cable_control.repository;

import home.local.cable_control.model.CableJournal;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CableJournalRepository extends JpaRepository<CableJournal, Long> {

    @Modifying
    @Transactional
    @Query("""
        UPDATE CableJournal c
        SET c.actualStatus = :actualStatus
        where c.ship.id = :ship_id
        and c.journalNum = :journalNum
    """)
    int updateActualStatusForAll(@Param("actualStatus") int actualStatus
            ,@Param("ship_id") long ship_id
            ,@Param("journalNum") String journalNum);

    List<CableJournal> findAllByOrderByJournalNumAscIdAsc();
}
