package com.linguo.customasset.service;

import com.linguo.customasset.convertor.CustomAssetViewConverter;
import com.linguo.customasset.dao.CustomAssetDao;
import com.linguo.customasset.model.CustomAsset;
import com.linguo.customasset.model.CustomAssetView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
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
    private CustomAssetViewConverter customAssetViewConverter;

    public CustomAsset search(String searchName) throws IOException {
        searchName = searchName.toLowerCase();
        CustomAsset customAsset = customAssetDao.searchByName(searchName);

        if(customAsset !=null){
            return customAsset;
        }else{
            return customAssetDao.save(searchName, null);
        }
    }

    public Page<CustomAssetView> list(Pageable pageable) {
        return customAssetDao.list(pageable).map(customAssetViewConverter);
    }
}
