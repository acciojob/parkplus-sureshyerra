package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();


        int cost = reservation.getSpot().getPricePerHour()*reservation.getNumberOfHours();

        if(amountSent < cost){
            throw  new Exception("Insufficient Amount");
        }
        String updatedmode = mode.toUpperCase();
        Payment payment = new Payment();
        payment.setPaymentCompleted(false);

        if(updatedmode.equals("CASH")) {
            payment.setPaymentMode(PaymentMode.CASH);
        } else if (updatedmode.equals("UPI")) {
            payment.setPaymentMode(PaymentMode.UPI);

        }
        else if(updatedmode.equals("CARD")){
            payment.setPaymentMode(PaymentMode.CARD);
        }
        else {
            throw new Exception("Payment mode not detected");
        }
        reservation.getSpot().setOccupied(true);
        reservation.setPayment(payment);
        payment.setReservation(reservation);
        payment.setPaymentCompleted(true);

        reservationRepository2.save(reservation);

        return payment;
    }
}
