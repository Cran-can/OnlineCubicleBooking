package com.capgemini.seetbooking.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.seetbooking.exception.DuplicateSeatNumberException;
import com.capgemini.seetbooking.exception.SeatNotFoundException;
import com.capgemini.seetbooking.model.Seat;
import com.capgemini.seetbooking.repository.SeatRepository;


@Service
public class SeatService {
 @Autowired
 private SeatRepository seatRepository;

 
 public String createOrUpdateSeat(Seat seat) {
	    // If the seat has an ID, it means it already exists in the database
	    // Perform an update, otherwise, create a new seat
	    if (seat.getId() != null) {
	        updateSeat(seat);
	        return "Seat updated";
	    } else {
	        if (isSeatNumberExists(seat)) {
	            throw new DuplicateSeatNumberException("Seat number already exists");
	        }
	        createSeat(seat);
	        return "Seat created";
	    }
	}

	private boolean isSeatNumberExists(Seat seat) {
	    // Check if a seat with the given seat number already exists in the room
	    return seatRepository.existsByRoomAndSeatNumber(seat.getRoom(), seat.getSeatNumber());
	}

	private Seat createSeat(Seat seat) {
	    // Implement logic to create a new seat
	    validateSeat(seat); // Perform any validation checks
	    
	    // Set any default values or perform other pre-save logic
	    
	    return seatRepository.save(seat);
	}

	private Seat updateSeat(Seat seat) {
	    // Implement logic to update an existing seat
	    // Fetch the existing seat from the database
	    Optional<Seat> existingSeatOptional = seatRepository.findById(seat.getId());

	    if (existingSeatOptional.isPresent()) {
	        Seat existingSeat = existingSeatOptional.get();
	        
	        // Check if the seat number is being changed to one that already exists in the room
	        if (!existingSeat.getSeatNumber().equals(seat.getSeatNumber()) && isSeatNumberExists(seat)) {
	            throw new DuplicateSeatNumberException("Seat number already exists");
	        }

	        // Update the properties of the existing seat
	        existingSeat.setSeatNumber(seat.getSeatNumber());
	        // Add other properties as needed
	        
	        // Save the updated seat to the database
	        return seatRepository.save(existingSeat);
	    } else {
	        throw new SeatNotFoundException("Seat not found");
	    }
	}

	private void validateSeat(Seat seat) {
	    // Implement validation logic for the seat
	    // For example, check that required fields are not null or empty
	    if (seat.getSeatNumber() == null || seat.getSeatNumber().trim().isEmpty()) {
	        throw new IllegalArgumentException("Seat number cannot be null or empty");
	    }
	    // Add other validation checks as needed
	}
}

