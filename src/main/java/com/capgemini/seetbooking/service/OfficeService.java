package com.capgemini.seetbooking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.seetbooking.dto.BookingDto;
import com.capgemini.seetbooking.model.Booking;
import com.capgemini.seetbooking.model.BookingStatus;
import com.capgemini.seetbooking.model.Office;
import com.capgemini.seetbooking.model.Seat;
import com.capgemini.seetbooking.model.SeatStatus;
import com.capgemini.seetbooking.model.User;
import com.capgemini.seetbooking.repository.BookingRepository;
import com.capgemini.seetbooking.repository.OfficeRepository;
import com.capgemini.seetbooking.repository.SeatRepository;
import com.capgemini.seetbooking.repository.UserRepository;

@Service
public class OfficeService {
	@Autowired
	private OfficeRepository officeRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SeatRepository seatRepository;

	public Office createOrUpdateOffice(Office office) {
		return officeRepository.save(office);
	}

	public List<Office> getAllOffices() {
		return officeRepository.findAll();
	}

	public Optional<Office> getOfficeById(Long officeId) {
		return officeRepository.findById(officeId);
	}

	public List<BookingDto> getAllBookings() {
		List<BookingDto> bookings = new ArrayList<>();
		
		BookingDto bDto = new BookingDto();
		List<Booking> existingbookings = bookingRepository.findAll();
		for(Booking booking : existingbookings) {
			bDto.setBookingId(booking.getId());
			bDto.setBookingStatus(booking.getStatus());
			bDto.setStartTime(booking.getStartTime());
			bDto.setEndTime(booking.getEndTime());
			bookings.add(bDto);
		}
		

		return bookings;
	}

	public String createBooking(Long userId, Long seatId, LocalDateTime startTime, LocalDateTime endTime,
			BookingStatus status) {
		Booking booking = new Booking();

		// Fetch user and seat entities from the database
		Optional<User> user = userRepository.findById(userId);
		Optional<Seat> seat = seatRepository.findById(seatId);
		Optional<Booking> existingBooking = bookingRepository.findByUserIdAndStatus(userId, BookingStatus.APPROVED);
		// Validate user, seat, and booking
		if (user.isEmpty()) {
			throw new RuntimeException("User not found");
		}

		if (seat.isEmpty() ) {
			throw new RuntimeException("Seat Not Available");
		}

		if (existingBooking.isPresent()) {
			throw new IllegalArgumentException("User already has a booked seat");
		}

		List<Booking> existingbookings = bookingRepository.findAll();
		for(Booking booked : existingbookings) {
		if(booked.getSeat().getId().equals(seatId) && (booked.getStatus()==BookingStatus.PENDING) || (booked.getStatus()==BookingStatus.APPROVED))
		{	
	         return "Seat is Freezed";
		}
		}
			booking.setUser(user.get());
	        booking.setSeat(seat.get());
	        booking.setStartTime(startTime);
	        booking.setEndTime(endTime);
	        booking.setStatus(status);
	        
	        bookingRepository.save(booking);
	        return "Successfully booked";
        // Save the booking to the database
		
		}

	
	public String approveBooking(Long bookingId) {
		Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
		if (optionalBooking.isPresent()) {
			Booking booking = optionalBooking.get();
			booking.getSeat().setStatus(SeatStatus.BOOKED);
			booking.setStatus(BookingStatus.APPROVED);
			bookingRepository.save(booking);
			return "Updated";
		} else {
			throw new RuntimeException("Booking not found");
		}
	}
	
	public String updateBooking() {
//		Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
		List<Booking> bookings = bookingRepository.findAll();
		List<Booking> bookingToRemove = new ArrayList<>();
		for(Booking book : bookings) {
			if(book.getEndTime().isBefore(LocalDateTime.now())) {
				Seat seat = book.getSeat();
				seat.setStatus(SeatStatus.OPEN);
				seatRepository.save(seat);
				
			    bookingToRemove.add(book);
			}
		}
		 for (Booking book : bookingToRemove) {
		        bookings.remove(book);
		        bookingRepository.delete(book);
		    }
		return "Updated";
	}
}

	// Add other methods as needed
