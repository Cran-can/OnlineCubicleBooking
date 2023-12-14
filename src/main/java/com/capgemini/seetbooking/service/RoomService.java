package com.capgemini.seetbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.seetbooking.model.Room;
import com.capgemini.seetbooking.repository.RoomRepository;

//com.capgemini.seetbooking.service.RoomService.java

@Service
public class RoomService {
 @Autowired
 private RoomRepository roomRepository;

 public Room createOrUpdateRoom(Room room) {
     // Implement logic to create or update a room
     return roomRepository.save(room);
 }

 // Implement other room-related methods as needed
}

