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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/orders/{idmesa}")
@Service
public class OrdersAPIController {

    @Autowired
    RestaurantOrderServices services;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getOrders(@PathVariable String idmesa) throws JsonProcessingException {
        //obtener datos que se enviarán a través del API
        try {
            Set<Integer> two = services.getTablesWithOrders();
            Map<Integer, String> listaord = new HashMap<Integer, String>();
            int id = Integer.parseInt(idmesa);
            listaord.put(id, services.getTableOrder(id).toString());

            String jsonformat = new ObjectMapper().writeValueAsString(listaord);
            return new ResponseEntity<>(jsonformat, HttpStatus.ACCEPTED);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(OrdersAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error 404", HttpStatus.NOT_FOUND);
        }

    }
}
