package edu.eci.arsw.myrestaurant.test;

import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.services.OrderServicesException;
import edu.eci.arsw.myrestaurant.services.RestaurantOrderServices;
import edu.eci.arsw.myrestaurant.services.RestaurantOrderServicesStub;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ApplicationServicesTests {

    @Autowired
    RestaurantOrderServices services;

    @Test
    public void IfTableDoesNotExistNotCalculateTotal() throws OrderServicesException {
        try {
            int total = services.calculateTableBill(8);
        } catch (OrderServicesException e) {
            assertEquals("Mesa inexistente o ya liberada:" + 8, e.getMessage());
        }
    }

    @Test
    public void IfTableExistCalculateTotal() throws OrderServicesException {

        assertEquals(services.calculateTableBill(1),45302);
        assertEquals(services.calculateTableBill(3),32290);
    }
}


