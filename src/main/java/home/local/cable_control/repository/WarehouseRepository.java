package home.local.cable_control.repository;

import home.local.cable_control.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query(value = """
            select *
            from warehouse w
            where (w.number, w.num1c, w.mark) in (
                select t.number, t.num1c, t.mark
                from unnest(
                    cast(:numbers as int[]),
                    cast(:num1cs as text[]),
                    cast(:marks as text[])
                ) as t(number, num1c, mark)
            )
            """, nativeQuery = true)
    List<Warehouse> findWarehouseByParam(
            @Param("numbers") Integer[] numbers,
            @Param("num1cs") String[] num1cs,
            @Param("marks") String[] marks
    );

    @Modifying
    @Query(value = """
            update Warehouse w
            set w.status = 0
            where w.id not in :id
                    """)
    void deactivateNotInIds(@Param("id") List<Long> id);

}
