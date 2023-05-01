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

        for(Spot spot : parkingLot.getSpotList()){

            if(spot.getOccupied() == false){
                if(spot.getSpotType() == SpotType.TWO_WHEELER){
                    if(numberOfWheels <= 2){
                        if(spot.getPricePerHour() < min){
                            min = spot.getPricePerHour();
                            newSpot = spot;
                        }
                    }
                } else if (spot.getSpotType() == SpotType.FOUR_WHEELER) {
                    if (numberOfWheels <= 4){
                        if (spot.getPricePerHour() < min){
                            min = spot.getPricePerHour();
                            newSpot = spot;
                        }
                    }

                }
                else {
                    if (numberOfWheels > 4) {
                        if (spot.getPricePerHour() < min) {
                            min = spot.getPricePerHour();
                            newSpot = spot;
                        }
                    }
                }
            }
        }
        if (newSpot == null){
            throw new Exception("Cannot make reservation");
        }

        newSpot.setOccupied(true);
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);
        reservation.setSpot(newSpot);
        newSpot.getReservationList().add(reservation);
        user.getReservationList().add(reservation);

        userRepository3.save(user);

        spotRepository3.save(newSpot);


        return reservation;





    }
}
