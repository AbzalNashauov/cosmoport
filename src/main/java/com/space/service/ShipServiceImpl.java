package com.space.service;


import com.space.model.entity.Ship;
import com.space.repository.ShipRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;


@Service("shipService")
public class ShipServiceImpl implements ShipService {


    private   final ShipRepository shipRepository;

    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }




    @Override
    public List<Ship> getShips(Specification<Ship> specification) {
        return shipRepository.findAll(specification);
    }


    @Override
    public Ship createShip(Ship ship) {
        if (isValidShip("create", ship)) {
            Double rating = calcRating(ship);
            ship.setRating(rating);
            if (ship.getUsed() == null) {
                ship.setUsed(false);
            }
            return shipRepository.save(ship);
        } else return null;
    }


    @Override
    @Transactional
    public Ship getShipById(Long id){
        return shipRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Ship> getAllShips(Specification<Ship> specification, Pageable pageable) {
        return shipRepository.findAll(specification, pageable);
    }

    @Override
    public void deleteShip(Long id) {
        shipRepository.deleteById(id);
    }



    private double calcRating(Ship ship) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        int prodYear = calendar.get(Calendar.YEAR);
        double usedRate = (ship.getUsed() == null ? 1 : (ship.getUsed())? 0.5 : 1);
        double shipSpeed = ship.getSpeed();
        BigDecimal rating =  BigDecimal.valueOf((80 * shipSpeed * usedRate)/ (3019 - prodYear + 1));
        rating = rating.setScale(2, RoundingMode.HALF_UP);
        return rating.doubleValue();

    }

    public boolean isValidShip(String operation, Ship ship) {
        switch (operation) {
            case "create": {

                if (ship.getName() != null && ship.getPlanet() != null && ship.getProdDate() != null &&
                        ship.getShipType() != null && ship.getSpeed() != null) {
                    isValidShip("update", ship);

                }
                else
                    return false;
            }
            case "update": {
                if (ship.getName() != null && (ship.getName().length() > 50 || ship.getName().length() < 1)) {
                    return false;
                }
                if (ship.getPlanet() != null && (ship.getPlanet().length() > 50 || ship.getPlanet().length() < 1)) {
                    return false;
                }
                if (ship.getSpeed() != null && (ship.getSpeed() > 0.99 || ship.getSpeed() < 0.01)) {
                    return false;
                }
                if (ship.getCrewSize() != null && (ship.getCrewSize() > 9999 || ship.getCrewSize() < 1)) {
                    return false;
                }
                Calendar calendar = Calendar.getInstance();
                if (ship.getProdDate() != null) {
                    if (ship.getProdDate().getTime() < 0) {
                        return false;
                    } else {
                        calendar.setTime(ship.getProdDate());
                        int prodYear = calendar.get(Calendar.YEAR);
                        return prodYear <= 3019 && prodYear >= 2800;
                    }
                }
                return true;
            }
            default: {
                return false;
            }

        }
    }
    @Override
    public Long checkId(String id) {
       long result;

        try {
          result = Long.parseLong(id);
       } catch (Exception e) {
            return -1L;
        }
       if (result == 0L) return -1L;
       if (!shipRepository.findById(result).isPresent())
           return 0L;
       else {

           return result;
       }
    }

    @Override
    public Ship updateShip(Long id, Ship ship) {

        if (shipRepository.findById(id).isPresent() && isValidShip("update", ship)) {
            Ship updatedShip = shipRepository.findById(id).get();
            if (ship.getName() != null)
                    updatedShip.setName(ship.getName());
            if (ship.getPlanet() != null)
                updatedShip.setPlanet(ship.getPlanet());
            if (ship.getSpeed() != null)
                updatedShip.setSpeed(ship.getSpeed());
            if (ship.getCrewSize() != null)
                updatedShip.setCrewSize(ship.getCrewSize());
            if (ship.getUsed() != null)
                updatedShip.setUsed(ship.getUsed());
            if (ship.getProdDate() != null)
                updatedShip.setProdDate(ship.getProdDate());
            if (ship.getShipType() != null)
                updatedShip.setShipType(ship.getShipType());

            double newRating = calcRating(updatedShip);
            updatedShip.setRating(newRating);
            return shipRepository.save(updatedShip);

        } else return null;
    }
}
