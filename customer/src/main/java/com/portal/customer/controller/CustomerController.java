package com.portal.customer.controller;

import com.portal.customer.bean.MyData;
import org.springframework.web.bind.annotation.*;


@RestController
public class CustomerController {

    @GetMapping(value = "/api/v1/customer")
    public String handleGetRequest() {
       return "hello from customer";
    }

    @GetMapping("/api/v2/customer/{name}/{age}")
    public String handleGetRequestWithPathParams(
            @PathVariable String name,
            @PathVariable int age) {
        return "Received GET request with path parameters: Name=" + name + ", Age=" + age;
    }
    @PostMapping("/api/v3/customer")
    public String handlePostRequest(@RequestBody MyData myData) {
        return "Received POST request with data: " + myData.getName() + ", " + myData.getAge() + " years old.";
    }

    @PutMapping("/api/v4/customer")
    public String handlePutRequest(@RequestBody MyData myData) {
        return "Received PUT request with data: " + myData.getName() + ", " + myData.getAge() + " years old.";
    }
}
