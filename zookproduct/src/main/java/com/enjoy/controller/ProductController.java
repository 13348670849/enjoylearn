package com.enjoy.controller;

import com.enjoy.dao.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("productcontroller")
public class ProductController {

    @RequestMapping("/getProduct/{id}")
    public Object getProduct(HttpServletRequest request, @PathVariable("id") String id) {
        return new Product(id,"name:"+request.getLocalPort());
    }


}
