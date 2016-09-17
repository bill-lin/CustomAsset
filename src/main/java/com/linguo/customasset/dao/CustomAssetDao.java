package com.linguo.customasset.dao;

import com.linguo.customasset.model.CustomAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
/**
 * Created by bin on 16/09/2016.
 */
@Repository
public class CustomAssetDao {
    @Autowired
    private CustomAssetRepository repository;

    @Autowired
    private MongoOperations mongo;

    public CustomAsset searchByName(String searchName) {
        //search and increase count by 1
        CustomAsset customAsset = mongo.findAndModify(
                query(where(CustomAsset.SEARCH_NAME).is(searchName)),
                new Update().inc(CustomAsset.SEARCH_COUNT, 1).set(CustomAsset.UPDATED_AT, new Date()),
                options().returnNew(true),
                CustomAsset.class);
        return customAsset;
    }

    public CustomAsset save(CustomAsset customAsset){
        return repository.save(customAsset);


    }

    public Page<CustomAsset> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
