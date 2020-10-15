package com.enjoy.web.controller;

import com.enjoy.web.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/doOrder", method = RequestMethod.GET)
    public String doOrder(@RequestParam("idcard") String idcard){
        try {
            String busId = UUID.randomUUID().toString();
            orderService.doOrder(busId,idcard);
            return "doOrder successfully";
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            e.printStackTrace();
            return e.getMessage();
        }
    }
}
