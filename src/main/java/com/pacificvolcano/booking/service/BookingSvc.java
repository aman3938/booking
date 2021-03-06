package com.pacificvolcano.booking.service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pacificvolcano.booking.domain.BookingStatus;
import com.pacificvolcano.booking.dto.BookingDto;
import com.pacificvolcano.booking.jpa.Booking;
import com.pacificvolcano.booking.jpa.BookingDate;
import com.pacificvolcano.booking.repository.BookingDateRepository;
import com.pacificvolcano.booking.repository.BookingRepository;

@Service
public class BookingSvc {
  @Autowired
  private BookingRepository bookingRepository;
  @Autowired
  private BookingDateRepository bookingDateRepository;
  
  @Transactional(readOnly = true)
  public List<LocalDate> getAvailableDates(LocalDate fromDate, LocalDate toDate){
      if(fromDate.isBefore(LocalDate.now())) {
          fromDate = LocalDate.now();
      }
      
      if(toDate.isAfter(LocalDate.now().plusDays(29))) {
          toDate = LocalDate.now().plusDays(29);
      }
      
      if(fromDate.isAfter(toDate)) {
          fromDate = toDate;
      }
      
      List<LocalDate> availableDates = new ArrayList<>();
      for(LocalDate i = fromDate; i.compareTo(toDate) <=0; i = i.plusDays(1)) {
          availableDates.add(i);
      }
      
      List<BookingDate> allDates = bookingDateRepository.findByCampsiteDateGreaterThan(fromDate.minusDays(1));
      for(BookingDate date: allDates) {
          if(BookingStatus.BOOKED.equals(date.getStatus())) {
              availableDates.remove(date.getCampsiteDate());
          }
      }
      return availableDates;
  }
  
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
  
  @Transactional
  public BookingDto updateBooking(long id, BookingDto dto) {
      Booking booking = bookingRepository.findById(id).orElseThrow(() -> new InvalidParameterException("Booking not found"));
      if(booking.getUpdatedAt() != null && !booking.getUpdatedAt().equals(dto.getUpdatedAt())) {
          throw new IllegalStateException("Booking has been updated in meantime. Please fetch and update again");
      }
      LocalDate fromDate = dto.getFromDate();
      LocalDate toDate = dto.getToDate();
      
      Set<BookingDate> oldDates = new HashSet<>(booking.getBookingDates());
      for (BookingDate date: oldDates) {
          booking.removeBookingDate(date);
      }
      
      validateToAndFromDate(fromDate, toDate);        
      for(LocalDate i = booking.getFromDate(); i.compareTo(booking.getToDate()) <= 0; i = i.plusDays(1)) {
          booking.addBookingDate(new BookingDate(i));
      }
      booking.setEmail(dto.getEmail());
      booking.setFirstName(dto.getFirstName());
      booking.setLastName(dto.getLastName());
      
      booking = bookingRepository.save(booking);
      return BookingDto.from(booking);
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
