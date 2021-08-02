package com.pacificvolcano.booking.controller;

import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pacificvolcano.booking.dto.BookingDto;
import com.pacificvolcano.booking.service.BookingSvc;

@RestController
public class BookingController {
  
  @Autowired
  private BookingSvc bookingSvc;
  
  @GetMapping(path="/booking/{id}")
  public ResponseEntity<?> getBooking(@PathVariable("id") long id){
      return bookingSvc.getBooking(id)
              .map(booking -> ResponseEntity.ok().body(BookingDto.from(booking)))
              .orElse(ResponseEntity.notFound().build());
  }
  
  @PostMapping(path="/booking")
  public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto){
      try {
          return ResponseEntity.status(HttpStatus.CREATED).body(bookingSvc.createBooking(bookingDto));
      } catch (InvalidParameterException e) {
          return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
      }catch (Exception e) {
          return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
      }       
  }
  
  @PutMapping(path="/booking/{id}")
  public ResponseEntity<?> updateBooking(@PathVariable("id") long id, 
                                          @RequestBody BookingDto bookingDto){
      try {
          BookingDto dto = bookingSvc.updateBooking(id, bookingDto);
          return ResponseEntity.status(HttpStatus.CREATED).body(dto);
      } catch (NoSuchElementException | InvalidParameterException e) {
          return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
      } catch(IllegalStateException e) {
          return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
      }
      catch (Exception e) {
          return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
      }
  }
  
  @DeleteMapping(path="/booking/{id}")
  public ResponseEntity<?> deleteBooking(@PathVariable("id") long id){        
      try {
          bookingSvc.deleteBooking(id);
          return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      } catch (NoSuchElementException e) {
          return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
      }catch (Exception e) {
          return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
      }   
  }

}
