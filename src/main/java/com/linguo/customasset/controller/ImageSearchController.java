package com.linguo.customasset.controller;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.linguo.customasset.exception.ImageSearchException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bin on 05/09/2016.
 */
@RestController
public class ImageSearchController {
    @Value("${customsearch.key}")
    private String customSearchKey;
    @Value("${customsearch.id}")
    private String customSearchId;

    @RequestMapping("/search")
    public  List<Result> searchImages(@RequestParam(value="query", defaultValue="World") String query) throws ImageSearchException {
        try {
            Search results = searchImage(query);
            List<Result.Image> images=new ArrayList<Result.Image>();
            return  results.getItems();
        }
        catch (  IOException e) {
            throw new ImageSearchException(e.getMessage(),e.getCause());
        }
    }

    @RequestMapping(value ="/getImage", produces="image/png")
    public  byte[] getSingleImage(@RequestParam(value="query") String query) throws ImageSearchException {
        try {
            Search results = searchImage(query);
            List<Result> imageItems = results.getItems();
           for(Result imageItem: imageItems){
               return getImage(imageItem.getLink());
           }
            throw new ImageSearchException("no result", null);
        }
        catch (  IOException e) {
            throw new ImageSearchException(e.getMessage(),e.getCause());
        }
    }

    private byte[] getImage(String link) throws IOException {
        ;
//        return IOUtils.toByteArray((new URL(link)).openStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Thumbnails.of(new URL(link)).size(240, 240).outputFormat("png").outputQuality(1).toOutputStream(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }finally {
            byteArrayOutputStream.close();
        }
    }

    private Search searchImage(@RequestParam(value = "query") String query) throws IOException {
        query += " Cartoon";
        Customsearch customsearch=getCustomsearch();
        Customsearch.Cse.List list=customsearch.cse().list(query);
        list.setKey(customSearchKey);
        list.setCx(customSearchId);
        list.setSearchType("image");
//            list.setImgSize("medium"); // small, medium, large
        list.setImgType("clipart");
        list.setFileType("png");
        return list.execute();
    }

    private Customsearch getCustomsearch() {
        HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer(){
            @Override
            public void initialize(HttpRequest request) throws IOException {
            }
        };

        return new Customsearch(new NetHttpTransport(),new JacksonFactory(), httpRequestInitializer);
    }
}
