package com.linguo.customasset.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by bin on 17/09/2016.
 */
@Document(collection = "CustomAsset")
public class CustomAsset {
    public static final String SEARCH_NAME = "SearchName";
    public static final String SEARCH_COUNT = "SearchCount";
    public static final String UPDATED_AT = "updatedAt";
    @Id
    @Field("objectId")
    private String id;

    @CreatedDate
    @Field("createdAt")
    private Date createdDate;

    @LastModifiedDate
    @Field(UPDATED_AT)
    private Date lastModifiedDate;

    @Field(SEARCH_NAME)
    private String searchName;

    @Field(SEARCH_COUNT)
    private int searchCount;

    @Field("S3_Id")
    private String s3Id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public String getS3Id() {
        return s3Id;
    }

    public void setS3Id(String s3Id) {
        this.s3Id = s3Id;
    }

    @Override
    public String toString() {
        return "CustomAsset{" +
                "id='" + id + '\'' +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", searchName='" + searchName + '\'' +
                ", searchCount=" + searchCount +
                ", s3Id='" + s3Id + '\'' +
                '}';
    }
}
