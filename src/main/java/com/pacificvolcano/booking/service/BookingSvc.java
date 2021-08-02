package com.pacificvolcano.booking.service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pacificvolcano.booking.dto.BookingDto;
import com.pacificvolcano.booking.jpa.Booking;
import com.pacificvolcano.booking.jpa.BookingDate;
import com.pacificvolcano.booking.repository.BookingRepository;

@Service
public class BookingSvc {
  @Autowired
  private BookingRepository bookingRepository;
  
  @Transactional
  public Long createBooking(BookingDto bookingDto) {
      LocalDate fromDate = bookingDto.getFromDate();
      LocalDate toDate = bookingDto.getToDate();
      validateToAndFromDate(fromDate, toDate);
      Booking booking = new Booking(fromDate, toDate, bookingDto.getEmail() , bookingDto.getFirstName(), bookingDto.getLastName());
      for(LocalDate i = fromDate; i.compareTo(toDate) <= 0; i = i.plusDays(1)) {
          booking.addBookingDate(new BookingDate(i));
      }
      
      bookingRepository.save(booking);
      return booking.getId();
  }
  
  @Transactional(readOnly = true)
  public Optional<Booking> getBooking(long id) {
      return bookingRepository.findById(id);
  }
  
  @Transactional
  public void deleteBooking(long id) {
      Optional<Booking> booking = bookingRepository.findById(id);
      if(!booking.isPresent()) {
          throw new NoSuchElementException("This booking doesn't exist");
      }
      bookingRepository.delete(booking.get());
  }
  
  private void validateToAndFromDate(LocalDate fromDate, LocalDate toDate) {
    if(ChronoUnit.DAYS.between(LocalDate.now(), fromDate) > 30) {
        throw new InvalidParameterException("Please pick booking date of less than one month in advance");
    }
    
    if(ChronoUnit.DAYS.between(fromDate, toDate) >= 3) {
        throw new InvalidParameterException("You can only book for max of three days");
    }
    
    if(fromDate.isBefore(LocalDate.now()) || fromDate.isAfter(toDate)) {
        throw new InvalidParameterException("Please pick valid time period");
    }
  }

}
