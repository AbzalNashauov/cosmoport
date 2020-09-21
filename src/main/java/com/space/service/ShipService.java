package com.space.service;


import com.space.model.entity.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ShipService {

    public List<Ship> getShips(Specification<Ship> specification);
    public Ship createShip(Ship ship);
    public Ship getShipById(Long id);
    public Page<Ship> getAllShips(Specification<Ship> specification, Pageable pageable);
    public void deleteShip(Long id);
    public Long checkId(String id);
    public Ship updateShip(Long id, Ship ship);
    public boolean isValidShip(String operation, Ship ship);

}
