package com.linguo.customasset.dao;

import com.linguo.customasset.model.CustomAsset;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomAssetRepository extends MongoRepository<CustomAsset, String> {

    CustomAsset findBySearchName(String searchName);

}
