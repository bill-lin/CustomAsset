package com.linguo.customasset.model;

import lombok.Data;

/**
 * Created by bin on 17/09/2016.
 */
@Data
public class CustomAssetView {
    private String name;

    private int searchCount;

    private String created;

    private String updated;

    private String s3Id;

}
