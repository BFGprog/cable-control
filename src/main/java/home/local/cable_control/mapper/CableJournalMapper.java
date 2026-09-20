package home.local.cable_control.mapper;

import home.local.cable_control.model.CableJournal;
import home.local.cable_control.model.dto.CableJournalDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CableJournalMapper {

    public CableJournalDto toDto(CableJournal cableJournal){
        CableJournalDto cableJournalDto = new CableJournalDto();
        cableJournalDto.setId(cableJournal.getId());
        cableJournalDto.setJournalNum(cableJournal.getJournalNum());
        cableJournalDto.setNotice(cableJournal.getNotice());
        cableJournalDto.setNoticeDate(cableJournal.getNoticeDate());
        cableJournalDto.setActualStatus(cableJournal.getActualStatus());
        cableJournalDto.setStatus(cableJournal.getStatus());
        cableJournalDto.setDocumentApprovalStage(cableJournal.getDocumentApprovalStage());
        return cableJournalDto;
    }
}
