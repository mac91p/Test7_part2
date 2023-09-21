package pl.kurs.personapp.services;

import org.springframework.stereotype.Service;
import pl.kurs.personapp.exceptionhandling.exceptions.BadPositionException;
import pl.kurs.personapp.models.Employee;
import pl.kurs.personapp.models.Position;
import java.util.Set;

@Service
public class EmployeePositionsService {


    public Employee updatePositions(Employee employee, Position position) {
        Set<Position> positions = employee.getPositions();
        Position existingPosition = findPositionByIdAndVersion(employee, position.getId(), position.getVersion());
        Position lastPosition = findLastPosition(positions);
        if (existingPosition != null) {
            existingPosition.setName(position.getName());
            existingPosition.setSalary(position.getSalary());
            existingPosition.setStartOfWork(position.getStartOfWork());
            existingPosition.setEndOfWork(position.getEndOfWork());
            employee.setPositions(positions);
        } else {
            if (lastPosition.getEndOfWork() != null) {
                if (lastPosition.getEndOfWork().before(position.getStartOfWork())) {
                    Position newPosition = new Position();
                    newPosition.setName(position.getName());
                    newPosition.setSalary(position.getSalary());
                    newPosition.setStartOfWork(position.getStartOfWork());
                    positions.add(newPosition);
                    employee.setPositions(positions);
                    employee.setCurrentPosition(newPosition.getName());
                    employee.setCurrentSalary(newPosition.getSalary());
                } else {
                    throw new BadPositionException("Position can not be added");
                }
            } else {
                throw new BadPositionException("Position can not be added");
            }
        }
        return employee;
    }

    private Position findPositionByIdAndVersion(Employee employee, Long positionId, Long version) {
        return employee.getPositions().stream()
                .filter(position -> position.getId().equals(positionId) && position.getVersion().equals(version))
                .findFirst()
                .orElse(null);
    }

    private Position findLastPosition(Set<Position> positions) {
        Position last = null;
        for (Position position : positions) {
            if (last == null || position.getId() > last.getId()) {
                last = position;
            }
        }
        return last;
    }
}
