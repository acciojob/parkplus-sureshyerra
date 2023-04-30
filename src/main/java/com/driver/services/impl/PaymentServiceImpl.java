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

        if(!mode.equals("cash") || !mode.equals("card") || !mode.equals("upi")){
            throw new Exception("Payment mode not detected");
        }
        int cost = reservation.getSpot().getPricePerHour()*reservation.getNumberOfHours();

        if(amountSent < cost){
            throw  new Exception("Insufficient Amount");
        }

        Payment payment = new Payment();
        payment.setPaymentCompleted(true);
        if(mode.equals("cash")) {
            payment.setPaymentMode(PaymentMode.CASH);
        } else if (mode.equals("upi")) {
            payment.setPaymentMode(PaymentMode.UPI);

        }
        else{
            payment.setPaymentMode(PaymentMode.CARD);
        }
        reservation.setPayment(payment);
        payment.setReservation(reservation);

        reservationRepository2.save(reservation);

        return payment;
    }
}
