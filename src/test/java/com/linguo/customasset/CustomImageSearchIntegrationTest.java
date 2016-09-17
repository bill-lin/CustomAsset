package com.linguo.customasset;

import com.linguo.customasset.dao.AwsS3Dao;
import com.linguo.customasset.dao.CustomAssetRepository;
import com.linguo.customasset.model.CustomAsset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by bin on 17/09/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomImageSearchIntegrationTest {
    private static String SEARCH_QUERY = "Pokemon Go ball";
    private static String SEARCH_NAME= SEARCH_QUERY.toLowerCase();
    private static String SEARCH_FILE_KEY = SEARCH_QUERY.toLowerCase().replace(" ","_")+".png";

    @Autowired
    private TestRestTemplate restTemplate ;

    @Autowired
    private CustomAssetRepository customAssetRepository;

    @Autowired
    private AwsS3Dao awsS3Dao;

    @Before
    public void setup(){
        cleanup();
    }

    private void cleanup() {
        try {
            awsS3Dao.delete(SEARCH_FILE_KEY);
        }catch (Exception e){

        }
        CustomAsset customAsset = customAssetRepository.findBySearchName(SEARCH_NAME);
        if(customAsset != null) {
            customAssetRepository.delete(customAsset);
        }
    }

    @Test
    public void assetSearch_shouldReturnImageAndSaveToS3AndDB() throws IOException {
        //first time search
        byte[] result = this.restTemplate.getForObject("/asset/search?query="+SEARCH_QUERY, byte[].class);
        assertThat(result).isNotEmpty();

        //should save to database
        CustomAsset customAsset = customAssetRepository.findBySearchName(SEARCH_NAME);
        assertThat(customAsset).isNotNull();
        assertThat(customAsset.getId()).isNotNull();
        assertThat(customAsset.getS3Id()).isEqualTo(SEARCH_FILE_KEY);
        assertThat(customAsset.getSearchCount()).isEqualTo(1);
        assertThat(customAsset.getSearchName()).isEqualTo(SEARCH_NAME);
        assertThat(customAsset.getCreatedDate()).isNotNull();
        assertThat(customAsset.getLastModifiedDate()).isNotNull();

        //should save to s3
        byte[] s3Image = awsS3Dao.download(SEARCH_FILE_KEY);
        assertThat(s3Image).isEqualTo(result);

        //second time search
        byte[] result2 = this.restTemplate.getForObject("/asset/search?query="+SEARCH_QUERY, byte[].class);
        assertThat(result2).isEqualTo(result);

        //should update count in  database
        CustomAsset customAsset2 = customAssetRepository.findBySearchName(SEARCH_NAME);
        assertThat(customAsset2).isNotNull();
        assertThat(customAsset2.getId()).isNotNull();
        assertThat(customAsset2.getS3Id()).isEqualTo(SEARCH_FILE_KEY);
        assertThat(customAsset2.getSearchCount()).isEqualTo(2);
        assertThat(customAsset2.getSearchName()).isEqualTo(SEARCH_NAME);
        assertThat(customAsset2.getCreatedDate()).isNotNull();
        assertThat(customAsset2.getLastModifiedDate()).isNotNull();
        assertThat(customAsset2.getLastModifiedDate()).isAfter(customAsset2.getCreatedDate());

    }
}

