package home.local.cable_control.model.documentdto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CableRowTemp {

    private List<String> type = new ArrayList<>();;
    private List<String> mark = new ArrayList<>();;
    private String roomNameIn;
    private String roomNameOut;
    private List<String> tinnedCopperBraid = new ArrayList<>();;
    private List<String> note = new ArrayList<>();;

}
