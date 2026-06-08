package home.local.cable_control.repository;

import home.local.cable_control.model.Cable;
import home.local.cable_control.model.export.CableExport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CableRepository extends JpaRepository<Cable, Long> {

    Optional<Cable> findByIndex(String index);
    @Query("select c from Cable c where c.index in :indexes")
    List<Cable> findByIndexes(@Param("indexes") List<String> indexes);

    @Query(value = """
            select
             string_agg(w.length ||'', '; ' ORDER BY w.length desc) wareLength
            ,string_agg(w.note, '; ' ORDER BY w.length desc) as wareNotes
            ,string_agg(imr.mark_replace, '; ' ORDER BY imr.mark_replace) indexMarkRepl
            ,row_number() over (order by c.id) id
            ,c.complete
            ,c.created_date as createdDate
            ,c.dat_request as datRequest
            ,c.design_length as designLength
            ,c.device_in as deviceIn
            ,c.device_out as deviceOut
            ,c.electrical_schematic as electricalSchematic
            ,c.index as index
            ,c.isolated_routing as isolatedRouting
            ,c.limit_length as limitLength
            ,c.mark as mark
            ,c.measured_length as measuredLength
            ,c.note as note
            ,c.queue as queue
            ,c.request as request
            ,c.room_in as roomIn
            ,c.room_name_in as roomNameIn
            ,c.room_name_out as roomNameOut
            ,c.room_out as roomOut
            ,c.route as route
            ,c.status as status
            ,c.status_cable as statusCable
            ,c.tighten_date as tightenDate
            ,c.tighten_person as tightenPerson
            ,c.tinned_copper_braid as tinnedCopperBraid
            ,c.tinned_copper_braid_added as tinnedCopperBraidAdded
            ,c.typemor_me as typeMOrMe
            from cable c
            left join warehouse w on w.mark = c.mark
             and w.length >= coalesce(c.measured_length, c.design_length)
             and w.ship like '%451%'
             and c.tighten_date is null
             and c.status = false
             and w.status = 1
            left join index_mark_replace imr on imr.index = c.index
             and imr.ship like '%451%'
             and imr.status > 0
            group by c.id
            """, nativeQuery = true)
    List<CableExport> findAllCableExport();


}
