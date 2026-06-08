package home.local.cable_control.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(indexes = {
        @Index(name = "idx_index_repl", columnList = "index")
})
@Data
public class IndexMarkReplace {
    /*коротыши*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private String index;
    private String markOld;
    private String markReplace;
    private String markChoice;
    private double designLength;
    private String agreedMark;
    private Integer status;
    private int num;
    private String letterOutNum;
    private String letterOutDate;
    private String letterInNum;
    private String letterInDate;
    private String cancelMark;
    private String note;
    private String ship;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexMarkReplace that = (IndexMarkReplace) o;
        return Double.compare(that.designLength, designLength) == 0 && num == that.num && Objects.equals(index, that.index) && Objects.equals(markOld, that.markOld) && Objects.equals(markReplace, that.markReplace) && Objects.equals(markChoice, that.markChoice) && Objects.equals(letterInNum, that.letterInNum) && Objects.equals(letterInDate, that.letterInDate) && Objects.equals(letterOutNum, that.letterOutNum) && Objects.equals(letterOutDate, that.letterOutDate) && Objects.equals(ship, that.ship) && Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, markOld, markReplace, markChoice, designLength, num, letterInNum, letterInDate, letterOutNum, letterOutDate, ship, note);
    }
}
