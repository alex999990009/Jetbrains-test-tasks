package org.jetbrains.station;

import org.jetbrains.car.Car;
import org.jetbrains.car.PetrolCar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StationTest {

    @Test
    void testStation() {
        Car petrolCar = new PetrolCar(12, 1);
        StationsPool stationsPool = StationsPool.getInstance();
        assertEquals(10, stationsPool.getClosestGasStation(petrolCar).getLocation());

        petrolCar = new PetrolCar(52, 1);
        assertEquals(45, stationsPool.getClosestGasStation(petrolCar).getLocation());

        Car electricCar = new PetrolCar(22, 1);
        assertEquals(15, stationsPool.getClosestChargingStation(electricCar).getLocation());

        electricCar = new PetrolCar(62, 1);
        assertEquals(59, stationsPool.getClosestChargingStation(electricCar).getLocation());
    }
}
