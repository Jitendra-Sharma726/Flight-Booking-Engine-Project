package service;

import entity.Flight;
import entity.PaymentInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.FlightRepository;
import repository.PaymentRepository;

@Service
public class BookingService {

    private final FlightRepository flightRepository;
    private final PaymentRepository paymentRepository;

    public BookingService(
            FlightRepository flightRepository,
            PaymentRepository paymentRepository) {

        this.flightRepository = flightRepository;
        this.paymentRepository = paymentRepository;
    }

    public Flight getFlight(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
    }

    @Transactional
    public void bookFlight(PaymentInfo paymentInfo) {

        Flight flight = flightRepository.findById(paymentInfo.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        if (flight.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        if (paymentInfo.getAmount() > 500) {
            throw new RuntimeException("Payment exceeds limit");
        }

        paymentRepository.save(paymentInfo);
    }
}
