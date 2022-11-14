package org.jetbrains.person;

import org.jetbrains.car.Car;
import org.jetbrains.car.PetrolCar;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void testPerson() {
        Random random = new Random();
        for (int j = 0; j < 100; ++j) {
            Car car = new PetrolCar(100 * random.nextDouble(), 1);
            Person person = new Person(19, 100 * random.nextDouble(), 100 * random.nextDouble(), car);

            for (int i = 0; i < 10; ++i) {
                person.goToWork();
                assertTrue(car.getEnergyValue() >= 0 && car.getEnergyValue() <= 100);
                person.goToHome();
                assertTrue(car.getEnergyValue() >= 0 && car.getEnergyValue() <= 100);
            }
        }
    }

    @Test
    void testPersonAge() {
        Car car = new PetrolCar(5, 1);
        Person person = new Person(17, 3, 60, car);

        double location = car.getLocation();
        person.goToHome();
        assertEquals(location, car.getLocation());
    }

    @Test
    void testPersonChangeCar() {
        Car car = new PetrolCar(5, 2);
        Person person = new Person(18, 3, 90, car);

        assertThrows(RuntimeException.class, person::goToWork);
        car = new PetrolCar(5, 1);
        person.changeCar(car);

        person.goToWork();
        assertEquals(90, car.getLocation());
    }
}
