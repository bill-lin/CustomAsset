package com.linguo.customasset.controller;

import com.linguo.customasset.dao.CustomDao;
import com.linguo.customasset.dao.Customer;
import com.linguo.customasset.exception.ImageSearchException;
import com.linguo.customasset.model.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by bin on 03/09/2016.
 */
@RestController
public class SampleController {
    @Value("${greeting.template}")
    private String template;
    private final AtomicLong counter = new AtomicLong();


    @Autowired
    private CustomDao customDao;

    @RequestMapping(path = "/greeting", method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping(path ="/test", method = RequestMethod.GET)
    public List<Customer> testMongoDb(@RequestParam(value = "query") String query) throws ImageSearchException {
        return customDao.testCRUD();
    }
}

