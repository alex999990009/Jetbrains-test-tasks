package org.jetbrains.car;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarTest {

    @Test
    void carTest() {
        Car car = new PetrolCar(0, 1);

        assertTrue(car.needsEnergy(90));

        car.driveTo(90);
        assertEquals(10, car.getEnergyValue());
        assertEquals(90, car.getLocation());

        car.refuel();
        assertEquals(100, car.getEnergyValue());

        car.driveTo(10);
        assertEquals(20, car.getEnergyValue());
        assertEquals(10, car.getLocation());
    }
}
