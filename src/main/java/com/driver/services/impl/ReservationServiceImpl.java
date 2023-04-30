package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {


        ParkingLot parkingLot;
        try {
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        }catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        User user;
        try {
            user = userRepository3.findById(userId).get();
        }catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        int min = Integer.MAX_VALUE;
        Spot newSpot = null;
        boolean available = false;
        for(Spot spot : parkingLot.getSpotList()){

            if(spot.getPricePerHour() < min && spot.isOccupied() == false  ){
                min = spot.getPricePerHour();
                newSpot = spot;
            }
        }
            if(newSpot.getSpotType() == SpotType.FOUR_WHEELER && numberOfWheels > 2 && numberOfWheels<=4){
                available = true;
            } else if (newSpot.getSpotType() == SpotType.TWO_WHEELER && numberOfWheels <=2) {
                available = true;
            } else if (newSpot.getSpotType()==SpotType.OTHERS && numberOfWheels > 4) {
                available = true;
            }
        if (available == false){
            throw new Exception("Cannot make reservation");
        }
        newSpot.setOccupied(true);
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(newSpot);
        newSpot.getReservationList().add(reservation);
        user.getReservationList().add(reservation);

        spotRepository3.save(newSpot);


        return reservation;





    }
}
