package com.linguo.customasset.controller;

import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.linguo.customasset.exception.ImageSearchException;
import com.linguo.customasset.service.GoogleImageSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bin on 05/09/2016.
 */
@RestController
public class ImageSearchController {

    @Autowired
    private GoogleImageSearch googleImageSearch;

    @RequestMapping("/search")
    public  List<Result> searchImages(@RequestParam(value="query", defaultValue="World") String query) throws ImageSearchException {
        try {
            Search results = googleImageSearch.searchImage(query);
            List<Result.Image> images=new ArrayList<Result.Image>();
            return  results.getItems();
        }
        catch (  IOException e) {
            throw new ImageSearchException(e.getMessage(),e.getCause());
        }
    }

    @RequestMapping(value ="/getImage", produces="image/png")
    public  byte[] getSingleImage(@RequestParam(value="query") String query) throws ImageSearchException {
        return googleImageSearch.getSingleImage(query);
    }





}
