package com.linguo.customasset.service;

import com.linguo.customasset.convertor.CustomAssetViewConverter;
import com.linguo.customasset.dao.AwsS3Dao;
import com.linguo.customasset.dao.CustomAssetDao;
import com.linguo.customasset.exception.ImageSearchException;
import com.linguo.customasset.model.CustomAsset;
import com.linguo.customasset.model.CustomAssetView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by bin on 17/09/2016.
 */
@Service
public class CustomAssetSearchFacade {
    @Autowired
    private CustomAssetDao customAssetDao;

    @Autowired
    private AwsS3Dao awsS3Dao;

    @Autowired
    private GoogleImageSearch googleImageSearch;

    @Autowired
    private CustomAssetViewConverter customAssetViewConverter;

    public byte[]  search(String searchName, boolean saveToDB) throws IOException, ImageSearchException {
        searchName = searchName.toLowerCase();
        if(saveToDB) {
            CustomAsset customAsset = customAssetDao.searchByName(searchName);

            if (customAsset != null && customAsset.getS3Id() != null) {
                try {
                    return awsS3Dao.download(customAsset.getS3Id());
                } catch (Exception e) {
                    return searchAndSave(searchName, customAsset);
                }
            } else {
                return searchAndSave(searchName, customAsset);
            }
        }else{
            return googleImageSearch.getSingleImage(searchName);
        }
    }

    private byte[] searchAndSave(String searchName, CustomAsset customAsset) throws ImageSearchException {
        byte[] image = googleImageSearch.getSingleImage(searchName);
        uploadAndSave(searchName, customAsset, image);
        return image;
    }

    private void uploadAndSave(String searchName, CustomAsset customAsset, byte[] image) {
        String fileKey = awsS3Dao.upload(image, searchName);
        if(customAsset == null){
            customAsset = new CustomAsset(searchName, fileKey);
        }
        customAssetDao.save(customAsset);
    }

    public Page<CustomAssetView> list(Pageable pageable, String localName, int localPort) {
        Page<CustomAssetView> customAssetViews = customAssetDao.list(pageable).map(customAssetViewConverter);
        for(CustomAssetView customAssetView : customAssetViews.getContent()){
            if(customAssetView.getS3Id()!=null) {
                customAssetView.setUrl("http://" + localName + ":"+localPort+ "/asset/" + customAssetView.getS3Id());
            }
        }
        return customAssetViews;
    }

    public byte[] view(String s3Id) throws IOException {
        return awsS3Dao.download(s3Id);
    }
}
