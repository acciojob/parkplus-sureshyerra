package com.driver.services.impl;
import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);

       ParkingLot savedParkingLot =  parkingLotRepository1.save(parkingLot);
       return  savedParkingLot;





    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {


        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = new Spot();
        if (numberOfWheels > 4){
            spot.setSpotType(SpotType.OTHERS);
        }
        else if (numberOfWheels <= 2){
            spot.setSpotType(SpotType.TWO_WHEELER);
        } else if (numberOfWheels >2 && numberOfWheels<=4) {
          spot.setSpotType(SpotType.FOUR_WHEELER);
        }
        spot.setPricePerHour(pricePerHour);
        spot.setOccupied(false);

        spot.setParkingLot(parkingLot);
        parkingLot.getSpotList().add(spot);


        parkingLotRepository1.save(parkingLot);

        return spot;

    }

    @Override
    public void deleteSpot(int spotId) {
        spotRepository1.deleteById(spotId);




    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        List<Spot> spotList = parkingLot.getSpotList();
        List<Spot>newSpotList = new ArrayList<>();
        Spot updated = null;
        for (Spot spot : spotList){
            if(spot.getId() == spotId){
                spot.setPricePerHour(pricePerHour);
                updated = spotRepository1.save(spot);

            }
            newSpotList.add(spot);
        }
        updated.setParkingLot(parkingLot);
        parkingLot.setSpotList(newSpotList);
        parkingLotRepository1.save(parkingLot);
        return updated;

    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
         parkingLotRepository1.deleteById(parkingLotId);


    }
}
