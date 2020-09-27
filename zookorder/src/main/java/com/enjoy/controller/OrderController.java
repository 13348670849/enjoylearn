package com.enjoy.controller;


import com.enjoy.pojo.Order;
import com.enjoy.pojo.Product;
import com.enjoy.utils.LoadBalance;
import com.enjoy.utils.RamdomLoadBalance;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RequestMapping("/order")
@RestController
public class OrderController {

    @Resource
    private RestTemplate restTemplate;

    private LoadBalance loadBalance = new RamdomLoadBalance();

    @RequestMapping("/getOrder/{id}")
    public Object getOrder(@PathVariable("id") String id ) {
        Product product = restTemplate.getForObject(loadBalance.choseServiceHost() + "productcontroller/getProduct/1", Product.class);
        return new Order(id,"orderName",product);
    }
}
