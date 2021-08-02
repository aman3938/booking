package com.pacificvolcano.booking.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pacificvolcano.booking.jpa.Booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private String email;
    private String lastName;
    private String firstName;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate fromDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate toDate;
    
    private LocalDateTime updatedAt;
    
       
    public static BookingDto from(Booking booking) {
        return new BookingDto(booking.getEmail(), booking.getLastName(), booking.getFirstName(), booking.getFromDate(), booking.getToDate(), booking.getUpdatedAt());
    }
    
}
