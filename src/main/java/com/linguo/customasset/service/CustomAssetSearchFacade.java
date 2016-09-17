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

    public byte[]  search(String searchName) throws IOException, ImageSearchException {
        searchName = searchName.toLowerCase();
        CustomAsset customAsset = customAssetDao.searchByName(searchName);

        if(customAsset !=null && customAsset.getS3Id() !=null){
            return awsS3Dao.download(customAsset.getS3Id());
        }else{
            byte[] image = googleImageSearch.getSingleImage(searchName);
            uploadAndSave(searchName, customAsset, image);
            return image;
        }
    }

    private void uploadAndSave(String searchName, CustomAsset customAsset, byte[] image) {
        String fileKey = awsS3Dao.upload(image, searchName);
        if(customAsset == null){
            customAsset = new CustomAsset(searchName, fileKey);
        }
        customAssetDao.save(customAsset);
    }

    public Page<CustomAssetView> list(Pageable pageable) {
        return customAssetDao.list(pageable).map(customAssetViewConverter);
    }
}
