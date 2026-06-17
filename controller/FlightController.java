package controller;

import entity.Flight;
import entity.PaymentInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BookingService;

@RestController
@RequestMapping("/api")
public class FlightController {

    private final BookingService bookingService;

    public FlightController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{id}")
    public Flight getFlight(@PathVariable Long id) {
        return bookingService.getFlight(id);
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookFlight(
            @RequestBody PaymentInfo paymentInfo) {

        try {
            bookingService.bookFlight(paymentInfo);
            return ResponseEntity.ok("Booking Successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Booking Failed");
        }
    }
}
