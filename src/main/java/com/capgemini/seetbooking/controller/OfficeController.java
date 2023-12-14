package com.capgemini.seetbooking.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.seetbooking.dto.BookingDto;
import com.capgemini.seetbooking.model.Booking;
import com.capgemini.seetbooking.model.Office;
import com.capgemini.seetbooking.service.OfficeService;

@RestController
@RequestMapping("/api/admin/office")
public class OfficeController {
	@Autowired
	private OfficeService officeService;

	@PostMapping("/createOrUpdate")
	public ResponseEntity<Office> createOrUpdateOffice(@RequestBody Office office) {
		Office createdOrUpdatedOffice = officeService.createOrUpdateOffice(office);
		return new ResponseEntity<>(createdOrUpdatedOffice, HttpStatus.OK);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<Office>> getAllOffices() {
		List<Office> offices = officeService.getAllOffices();
		return new ResponseEntity<>(offices, HttpStatus.OK);
	}

	@GetMapping("/get/{officeId}")
	public ResponseEntity<Office> getOfficeById(@PathVariable Long officeId) {
		Optional<Office> office = officeService.getOfficeById(officeId);
		return office.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/bookings/getAll")
	public ResponseEntity<List<BookingDto>> getAllBookings() {

		return new ResponseEntity<List<BookingDto>>(officeService.getAllBookings(), HttpStatus.OK);
	}

	@PostMapping("/bookings/create")
	public ResponseEntity<String> createBooking(@RequestBody Booking booking) {
       return new ResponseEntity<>(officeService.createBooking(booking.getUser().getId(),
						booking.getSeat().getId(), booking.getStartTime(), booking.getEndTime(), booking.getStatus()),HttpStatus.OK);
	}
//	
	@PutMapping("/bookings/approve/{bookingId}")
    public ResponseEntity<String> approveBooking(@PathVariable Long bookingId) {
        try {
            String approvedBooking = officeService.approveBooking(bookingId);
            return new ResponseEntity<>(approvedBooking, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	@PutMapping("/bookings/update")
	public ResponseEntity<String> updateBookings()
	{
		return new ResponseEntity<String>(officeService.updateBooking(),HttpStatus.OK); 
	}
	
//	@PostMapping("/bookings/reject/{bookingId}")
//    public ResponseEntity<Booking> rejectBooking(@PathVariable Long bookingId) {
//        try {
//            Booking rejectedBooking = officeService.rejectBooking(bookingId);
//            return new ResponseEntity<>(rejectedBooking, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
		

	// Add other endpoints as needed
}

