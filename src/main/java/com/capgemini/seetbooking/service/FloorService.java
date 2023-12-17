package com.capgemini.seetbooking.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.seetbooking.exception.DuplicateFloorNumberException;
import com.capgemini.seetbooking.exception.FloorNotFound;
import com.capgemini.seetbooking.model.Floor;
import com.capgemini.seetbooking.repository.FloorRepository;


@Service
public class FloorService {
 @Autowired
 private FloorRepository floorRepository;

 public String create(Floor floor) {
	    // If the floor has an ID, it means it already exists in the database
	    // Perform an update, otherwise, create a new floor
	    if (floor.getId() != null) {
	        updateFloor(floor);
	        return "Floor is Updated";
	    } else {
	        if (isFloorNumberExists(floor.getFloorNumber())) {
	            throw new DuplicateFloorNumberException("Floor number already exists");
	        }
	        createFloor(floor);
	        return "Floor is created";
	    }
	}

	private boolean isFloorNumberExists(String floorNumber) {
	    // Check if a floor with the given floorNumber already exists
	    return floorRepository.existsByFloorNumber(floorNumber);
	}

	private Floor createFloor(Floor floor) {
	    // Implement logic to create a new floor
	    validateFloor(floor); // Perform any additional validation checks
	    return floorRepository.save(floor);
	}

	private Floor updateFloor(Floor floor) {
	    // Implement logic to update an existing floor
	    // Fetch the existing floor from the database
	    Optional<Floor> existingFloorOptional = floorRepository.findById(floor.getId());

	    if (existingFloorOptional.isPresent()) {
	        Floor existingFloor = existingFloorOptional.get();

	        // Check if the floor number is being changed to an already existing floor number
	        if (!existingFloor.getFloorNumber().equals(floor.getFloorNumber()) && isFloorNumberExists(floor.getFloorNumber())) {
	            throw new DuplicateFloorNumberException("Floor number already exists");
	        }

	        // Update the properties of the existing floor
	        existingFloor.setFloorNumber(floor.getFloorNumber());
	        // Add other properties as needed

	        // Save the updated floor to the database
	        return floorRepository.save(existingFloor);
	    } else {
	        throw new FloorNotFound("Floor not found");
	    }
	}

	private void validateFloor(Floor floor) {
	    // Implement any additional validation logic for the floor
	    // For example, check that required fields are not null or empty
	    if (floor.getFloorNumber() == null || floor.getFloorNumber().trim().isEmpty()) {
	        throw new IllegalArgumentException("Floor number cannot be null or empty");
	    }
	}
	
	
}

