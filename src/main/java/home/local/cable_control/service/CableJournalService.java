package home.local.cable_control.service;

import home.local.cable_control.mapper.CableJournalMapper;
import home.local.cable_control.model.CableJournal;
import home.local.cable_control.model.auxiliary.DocumentParameters;
import home.local.cable_control.model.dto.CableJournalDto;
import home.local.cable_control.repository.CableJournalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class CableJournalService {
    private final ShipService shipService;
    private final CableJournalRepository cableJournalRepository;
    private final CableJournalMapper cableJournalMapper;


    public List<CableJournalDto> getCableJournals() {
        List<CableJournal> cableJournals = cableJournalRepository.findAllByOrderByJournalNumAscIdAsc();
        return cableJournals.stream()
                .map(cableJournalMapper::toDto)
                .toList();
    }

    public CableJournal saveCableJournal(DocumentParameters parameters) {

        if (parameters.getDocumentId() != null) {
            return cableJournalRepository.findById(parameters.getDocumentId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Заказ не найден: " + parameters.getDocumentId()
                    ));
        }

        CableJournal cableJournal = setCableJournalCable(parameters);
        cableJournal.setShip(shipService.getOrCreate(parameters.getIncomingShip()));

        if (parameters.getActualStatus() == 1) {
            cableJournalRepository.updateActualStatusForAll(0
                    , cableJournal.getShip().getId()
                    , cableJournal.getJournalNum());
        }

        return cableJournalRepository.save(cableJournal);
    }

    private CableJournal setCableJournalCable(DocumentParameters parameters) {
        CableJournal cableJournal = new CableJournal();
        cableJournal.setJournalNum(parameters.getDocumentNum());
        cableJournal.setNotice(parameters.getNotice());
        cableJournal.setNoticeDate(parameters.getNoticeDate());
        cableJournal.setActualStatus(parameters.getActualStatus());
        cableJournal.setDocumentApprovalStage(parameters.getDocumentApprovalStage());

        return cableJournal;
    }


}
