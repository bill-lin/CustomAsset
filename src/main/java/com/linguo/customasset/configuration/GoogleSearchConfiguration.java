package com.linguo.customasset.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

/**
 * Created by bin on 17/09/2016.
 */
@Configuration
public class GoogleSearchConfiguration {

    @Value("${customsearch.key.id.pairs}")
    private String accessKeyIdPairs;

    @Autowired
    private ObjectMapper objectMapper;


    private List<GoogleAccessDetails> googleAccessDetailsList;

    private int counter = 0;


    private List<GoogleAccessDetails> getGoogleAccessDetailsList()  {
        if(googleAccessDetailsList == null) {
            try {
                googleAccessDetailsList = objectMapper.readValue(accessKeyIdPairs, new TypeReference<List<GoogleAccessDetails>>(){}) ;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return googleAccessDetailsList;
    }

    public GoogleAccessDetails getGoogleAccessDetails(){
        counter++;
        List<GoogleAccessDetails> googleAccessDetailsList = getGoogleAccessDetailsList();
        return googleAccessDetailsList.get(counter % googleAccessDetailsList.size());
    }
}