package com.pacificvolcano.booking.jpa;


import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.pacificvolcano.booking.domain.BookingStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class BookingDate {
    @Id
    private LocalDate campsiteDate;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Booking booking;
    
    public BookingDate(LocalDate date) {
        this.campsiteDate = date;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingDate )) return false;
        return campsiteDate != null && campsiteDate.equals(((BookingDate) o).getCampsiteDate());
    }
 
    @Override
    public int hashCode() {
        return campsiteDate.hashCode();
    }
}
