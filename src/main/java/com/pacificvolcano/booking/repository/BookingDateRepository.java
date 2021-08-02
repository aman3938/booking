package com.pacificvolcano.booking.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.pacificvolcano.booking.jpa.BookingDate;

public interface BookingDateRepository extends CrudRepository<BookingDate, LocalDate>{

	List<BookingDate> findByCampsiteDateGreaterThan(LocalDate minusDays);

}
