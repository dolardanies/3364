/*
 * Copyright (C) 2016 Pivotal Software, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.arsw.myrestaurant.restcontrollers;

import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.services.OrderServicesException;
import edu.eci.arsw.myrestaurant.services.RestaurantOrderServices;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/orders")
@Service
public class OrdersAPIController {

    @Autowired
    RestaurantOrderServices services;

    Gson g = new Gson();

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getOrders() {
        Map<String, Order> listaord = new HashMap<>();
        Set<Integer> two = services.getTablesWithOrders();
        two.forEach((i) -> {
            try {
                listaord.put(Integer.toString(i), services.getTableOrder(i));
            } catch (OrderServicesException ex) {
                Logger.getLogger(OrdersAPIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });    
        String listord_json = g.toJson(listaord);
        return new ResponseEntity<>(listord_json, HttpStatus.ACCEPTED);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/{idName}")
    public ResponseEntity<?> getOrder(@PathVariable String idName) {
        try {
            Map<String, Order> listaord = new HashMap<>();

            listaord.put(idName, services.getTableOrder(Integer.parseInt(idName)));
            String listaord_json = g.toJson(listaord);
            return new ResponseEntity<>(listaord_json, HttpStatus.ACCEPTED);
        } catch (OrderServicesException e) {
            Logger.getLogger(OrdersAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> postOrders(@RequestBody String jsonOrder) {

        try {
            Type listType = new TypeToken<Map<String, Order>>(){}.getType();
            Map<String, Order> listaord = g.fromJson(jsonOrder, listType);
            Set<String> keys = listaord.keySet();
            for (String s : keys) {
                System.out.println(s + "  ->   " + listaord.get(s));
                services.addNewOrderToTable(listaord.get(s));
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (OrderServicesException e) {
            Logger.getLogger(OrdersAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

}

