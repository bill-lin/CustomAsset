package com.linguo.customasset.convertor;

import com.linguo.customasset.model.CustomAsset;
import com.linguo.customasset.model.CustomAssetView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

/**
 * Created by bin on 17/09/2016.
 */
@Service
public class CustomAssetViewConverter implements Converter<CustomAsset, CustomAssetView> {
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public CustomAssetView convert(CustomAsset source) {
        if(source == null){
            return null;
        }

        CustomAssetView customAssetView = new CustomAssetView();
        customAssetView.setName(source.getSearchName());
        customAssetView.setSearchCount(source.getSearchCount());
        customAssetView.setCreated(source.getCreatedDate() == null ? null : format.format(source.getCreatedDate()));
        customAssetView.setUpdated(source.getLastModifiedDate() == null ? null : format.format(source.getLastModifiedDate()));
        customAssetView.setS3Id(source.getS3Id());
        return customAssetView;
    }
}
