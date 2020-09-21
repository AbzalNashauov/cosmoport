package com.space.specification;

import com.space.model.ShipType;
import com.space.model.entity.Ship;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;


public class ShipSpecification {

    public static Specification<Ship> getSpecification(String name,
                                                       String planet,
                                                       ShipType shipType,
                                                       Long after, Long before,
                                                       Boolean isUsed,
                                                       Double minSpeed, Double maxSpeed,
                                                       Integer minCrewSize, Integer maxCrewSize,
                                                       Double minRating, Double maxRating) {
        return Specification.where(ShipSpecification.shipsByName(name)
                .and(ShipSpecification.shipsByPlanet(planet)))
                .and(ShipSpecification.shipsByShipType(shipType))
                .and(ShipSpecification.shipsByBetweenDate(after, before))
                .and(ShipSpecification.shipsByUsage(isUsed))
                .and(ShipSpecification.shipsByBetweenSpeed(minSpeed, maxSpeed))
                .and(ShipSpecification.shipsByBetweenCrewSize(minCrewSize, maxCrewSize))
                .and(ShipSpecification.shipsByBetweenRating(minRating, maxRating));
    }

    public static Specification<Ship> shipsByName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) return null;
            return criteriaBuilder.like(root.get("name"), "%" + name +"%");
        };
    }

    public static Specification<Ship> shipsByPlanet(String planetName) {
        return (root, query, criteriaBuilder) -> {
            if (planetName == null) return null;
            return criteriaBuilder.like(root.get("planet"), "%" + planetName + "%");
        };
    }

    public static Specification<Ship> shipsByShipType(ShipType shipType) {
        return (root, query, criteriaBuilder) -> {
            if (shipType == null) return null;
            return criteriaBuilder.equal(root.get("shipType"), shipType);
        };
    }

    public static Specification<Ship> shipsByBetweenDate(Long after, Long before) {
        return (root, query, criteriaBuilder) -> {
            if (after == null && before == null) return null;
            if (after == null)  {
                Date beforeDate = new Date(before);
                return criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), beforeDate);
            }
            if (before == null) {
                Date afterDate = new Date(after);
                return criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), afterDate);
            }

            Date afterDate = new Date(after);
            Date beforeDate = new Date(before);
            return criteriaBuilder.between(root.get("prodDate"), afterDate, beforeDate);
        };
    }

    public static Specification<Ship> shipsByBetweenSpeed(Double minSpeed, Double maxSpeed) {
        if (minSpeed != null && maxSpeed != null) {
            return (root, query, cb) -> cb.between(root.get("speed"), minSpeed, maxSpeed);
        } else if (minSpeed != null) {
            return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("speed"), minSpeed);
        } else if (maxSpeed != null) {
            return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("speed"), maxSpeed);
        } else return null;
    }

    public static Specification<Ship> shipsByBetweenCrewSize(Integer minCrewSize, Integer maxCrewSize) {
        return (root, query, criteriaBuilder) -> {
          if (minCrewSize == null && maxCrewSize == null) return null;
          if (minCrewSize == null) return
                criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize);
          if (maxCrewSize == null) return
                criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize);
          return
                criteriaBuilder.between(root.get("crewSize"), minCrewSize, maxCrewSize);
        };
    }

    public static Specification<Ship> shipsByBetweenRating(Double minRating, Double maxRating) {
        if (minRating == null && maxRating == null) {
            return null;
        }
        if (minRating == null) return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating));
        if (maxRating == null) return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating));
        return ((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("rating"), minRating, maxRating));
    }


    public static Specification<Ship> shipsByUsage(Boolean isUsage) {
        if (isUsage != null) {
            if (isUsage) return ((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isUsed")));
            else
                return ((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isUsed")));
        }

        return null;
    }

    }