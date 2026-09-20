package home.local.cable_control.service;

import home.local.cable_control.model.Ship;
import home.local.cable_control.model.SqlQuery;
import home.local.cable_control.model.auxiliary.IncomingShip;
import home.local.cable_control.model.dto.Report;
import home.local.cable_control.repository.ShipRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShipService {
    private final ShipRepository shipRepository;


    public List<Ship> getShips() {
        return shipRepository.findAllByOrderByNumAscIdAsc();
    }

    public Ship getOrCreate(IncomingShip inShip) {
        if (inShip.getShipId() != null) {
            return shipRepository.findById(inShip.getShipId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Заказ не найден: " + inShip.getShipId()
                    ));
        }

        validateNewShip(inShip);
        boolean exists = shipRepository.existsByNameAndNumAndProjectNumAndProjectName(
                inShip.getName(),
                inShip.getNum(),
                inShip.getProjectNum(),
                inShip.getProjectName()
        );
        if (exists) {
            throw new IllegalStateException("Заказ уже существует");
        }

        Ship ship = new Ship();
        ship.setName(inShip.getName());
        ship.setNum(inShip.getNum());
        ship.setProjectNum(inShip.getProjectNum());
        ship.setProjectName(inShip.getProjectName());

        return shipRepository.save(ship);
    }


    private void validateNewShip(IncomingShip incoming) {

        if (incoming.getName() == null || incoming.getName().isBlank()) {
            throw new IllegalArgumentException("Не указано Наименование заказа");
        }

        if (incoming.getNum() == null || incoming.getNum().isBlank()) {
            throw new IllegalArgumentException("Не указан номер заказа");
        }

        if (incoming.getProjectNum() == null || incoming.getProjectNum().isBlank()) {
            throw new IllegalArgumentException("Не указан Проектный номер заказа");
        }

        if (incoming.getProjectName() == null || incoming.getProjectName().isBlank()) {
            throw new IllegalArgumentException("Не указано Проектное наименование заказа");
        }
    }

}
