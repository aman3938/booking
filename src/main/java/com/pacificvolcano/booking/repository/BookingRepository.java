package com.pacificvolcano.booking.repository;

import org.springframework.data.repository.CrudRepository;

import com.pacificvolcano.booking.jpa.Booking;

public interface BookingRepository extends CrudRepository<Booking, Long>{

}
