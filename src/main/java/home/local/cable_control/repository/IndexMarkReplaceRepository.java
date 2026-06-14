package home.local.cable_control.repository;

import home.local.cable_control.model.Cable;
import home.local.cable_control.model.IndexMarkReplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexMarkReplaceRepository extends JpaRepository<IndexMarkReplace, Long> {

    @Query("""
                select i.markReplace
                from IndexMarkReplace i
                where i.index = :index
                  and i.letterOutNum = :letterOutNum
                  and i.letterOutDate = :letterOutDate
                  and i.letterInNum = :letterInNum
                  and i.letterInDate = :letterInDate
            """)
    List<String> findMarkByIndexes(
            @Param("index") String index,
            @Param("letterOutNum") String letterOutNum,
            @Param("letterOutDate") String letterOutDate,
            @Param("letterInNum") String letterInNum,
            @Param("letterInDate") String letterInDate
    );

    List<IndexMarkReplace> findByIndex(String index);

    List<IndexMarkReplace> findByIndexAndStatusGreaterThan(String index, int status);


}
