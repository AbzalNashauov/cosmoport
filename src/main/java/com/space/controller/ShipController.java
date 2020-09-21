package com.space.controller;


import com.space.model.ShipType;
import com.space.model.entity.Ship;
import com.space.service.ShipService;
import com.space.specification.ShipSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import java.util.List;



@RestController
@RequestMapping("/rest")
public class ShipController {



    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    private final ShipService shipService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model){
        model.addAttribute("message", "Spring MVC Java Configuration Example");

        return "message";
    }
    @GetMapping("/ships")
    public List<Ship> getAllShips(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "planet", required = false) String planet,
                                  @RequestParam(value = "shipType", required = false) ShipType shipType,
                                  @RequestParam(value = "after", required = false) Long afterDate,
                                  @RequestParam(value = "before", required = false) Long beforeDate,
                                  @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                  @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                  @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                  @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                  @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                  @RequestParam(value = "minRating", required = false) Double minRating,
                                  @RequestParam(value = "maxRating", required = false) Double maxRating,
                                  @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,
                                  @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {
        Specification<Ship> shipSpecification = ShipSpecification.getSpecification(name, planet, shipType, afterDate, beforeDate, isUsed, minSpeed, maxSpeed,
                                                                                    minCrewSize, maxCrewSize, minRating, maxRating);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return shipService.getAllShips(shipSpecification, pageable).getContent();
    }

    @GetMapping("/ships/count")
    public Integer getShipCount(@RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "planet", required = false) String planet,
                                @RequestParam(value = "shipType", required = false) ShipType shipType,
                                @RequestParam(value = "after", required = false) Long afterDate,
                                @RequestParam(value = "before", required = false) Long beforeDate,
                                @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                @RequestParam(value = "minRating", required = false) Double minRating,
                                @RequestParam(value = "maxRating", required = false) Double maxRating)
            { Specification<Ship> shipSpecification = ShipSpecification.getSpecification(name, planet, shipType, afterDate, beforeDate,isUsed, minSpeed,
                                                                                         maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

            return shipService.getShips(shipSpecification).size();

    }

    @GetMapping("/ships/{shipId}")
    @ResponseBody
    public ResponseEntity<Ship> getById(@PathVariable(value = "shipId") String id) {
        Long longId = shipService.checkId(id);
        if (longId == -1L)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (longId == 0L)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else

            return new ResponseEntity<>(shipService.getShipById(longId), HttpStatus.OK);
    }

    @PostMapping("/ships")
    @ResponseBody
    public ResponseEntity<Ship> createShip(@RequestBody Ship newShip) {
        if (shipService.isValidShip("create", newShip))
            return new ResponseEntity<>(shipService.createShip(newShip), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/ships/{shipId}")
    public ResponseEntity<Ship> deleteShip(@PathVariable(value = "shipId") String id) {
        Long longId = shipService.checkId(id);
        if (longId == -1L) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (longId == 0L) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        shipService.deleteShip(longId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/ships/{shipId}")
    @ResponseBody
    public ResponseEntity<Ship> updateShip(@PathVariable(value = "shipId") String id, @RequestBody Ship ship) {
        Long longId = shipService.checkId(id);
        if (longId == -1L) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (longId == 0L) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (shipService.isValidShip("update", ship)) {
            return new ResponseEntity<>(shipService.updateShip(longId, ship), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}

