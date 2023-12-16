package com.capgemini.seetbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.capgemini.seetbooking.model.Seat;
import com.capgemini.seetbooking.service.SeatService;

//com.capgemini.seetbooking.controller.SeatController.java

@RestController
@RequestMapping("/api/admin/office/seat")
public class SeatController {
 @Autowired
 private SeatService seatService;
 
 

 @PostMapping("/create")
 public ResponseEntity<String> createOrUpdateSeat(@RequestBody Seat seat) {
     return new ResponseEntity<>(seatService.createOrUpdateSeat(seat), HttpStatus.OK);
 }

 // Implement other seat-related endpoints as needed
}
