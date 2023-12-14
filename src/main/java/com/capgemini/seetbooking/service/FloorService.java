package com.capgemini.seetbooking.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.seetbooking.model.Floor;
import com.capgemini.seetbooking.repository.FloorRepository;

//com.capgemini.seetbooking.service.FloorService.java

@Service
public class FloorService {
 @Autowired
 private FloorRepository floorRepository;

 public Floor createOrUpdateFloor(Floor floor) {
     // If the floor has an ID, it means it already exists in the database
     // Perform an update, otherwise, create a new floor
     if (floor.getId() != null) {
         return updateFloor(floor);
     } else {
         return createFloor(floor);
     }
 }
 private Floor createFloor(Floor floor) {
     // Implement logic to create a new floor
     return floorRepository.save(floor);
 }
 private Floor updateFloor(Floor floor) {
     // Implement logic to update an existing floor
     // Fetch the existing floor from the database
     Optional<Floor> existingFloorOptional = floorRepository.findById(floor.getId());

     if (existingFloorOptional.isPresent()) {
         Floor existingFloor = existingFloorOptional.get();
         
         // Update the properties of the existing floor
         existingFloor.setFloorNumber(floor.getFloorNumber());
         // Add other properties as needed
         
         // Save the updated floor to the database
         return floorRepository.save(existingFloor);
     } else {
         throw new RuntimeException("Floor not found");
     }
 }
 // Implement other floor-related methods as needed
}

