package com.linguo.customasset.service;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;
import com.linguo.customasset.configuration.GoogleAccessDetails;
import com.linguo.customasset.configuration.GoogleSearchConfiguration;
import com.linguo.customasset.exception.ImageSearchException;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
/**
 * Created by bin on 17/09/2016.
 */
@Service
public class GoogleImageSearch {
    private final Logger logger = LoggerFactory.getLogger(GoogleImageSearch.class);

    @Autowired
    private GoogleSearchConfiguration googleSearchConfiguration;

    public  byte[] getSingleImage( String searchName) throws ImageSearchException {
        try {
            Search results = searchImage(searchName);
            List<Result> imageItems = results.getItems();
            if(imageItems== null){
                throw new ImageSearchException("no result", null);
            }
            for(Result imageItem: imageItems){
                try {
                    final String link = imageItem.getLink();
                    logger.info("search name={}, url={}", searchName, link);
                    return getImage(link);
                }catch (Exception e){
                    continue;
                }
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

    public Search searchImage(String searchName) throws IOException {
        searchName += " Cartoon";
        Customsearch customsearch=getCustomsearch();
        Customsearch.Cse.List list=customsearch.cse().list(searchName);
        GoogleAccessDetails googleAccessDetails = googleSearchConfiguration.getGoogleAccessDetails();
        logger.debug("google account id= {}", googleAccessDetails.getId());
        list.setKey(googleAccessDetails.getKey());
        list.setCx(googleAccessDetails.getId());
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
