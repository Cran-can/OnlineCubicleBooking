package com.capgemini.seetbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.seetbooking.model.Seat;

//com.capgemini.seetbooking.repository.SeatRepository.java

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Add other queries as needed
}
