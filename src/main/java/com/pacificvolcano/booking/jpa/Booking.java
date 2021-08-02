package com.pacificvolcano.booking.jpa;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.pacificvolcano.booking.domain.BookingStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy="booking",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookingDate> bookingDates;
    
    private LocalDate fromDate;
    private LocalDate toDate;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Booking(LocalDate fromDate, LocalDate toDate, String email, String firstName, String lastName) {
        super();
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookingDates = new HashSet<>();
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public void addBookingDate(BookingDate date) {
        bookingDates.add(date);
        date.setBooking(this);
        date.setStatus(BookingStatus.BOOKED);
    }
    
    public void removeBookingDate(BookingDate date) {
        bookingDates.remove(date);
        date.setBooking(null);
        date.setStatus(BookingStatus.AVAILABLE);
    }
}
