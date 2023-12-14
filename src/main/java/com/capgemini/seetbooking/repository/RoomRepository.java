package com.capgemini.seetbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.seetbooking.model.Room;

//com.capgemini.seetbooking.repository.RoomRepository.java

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
 // Add custom queries if needed
}
