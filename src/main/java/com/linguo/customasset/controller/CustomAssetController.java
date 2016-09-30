package com.linguo.customasset.controller;

import com.linguo.customasset.exception.ImageSearchException;
import com.linguo.customasset.model.CustomAssetView;
import com.linguo.customasset.service.CustomAssetSearchFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by bin on 05/09/2016.
 */
@RestController
public class CustomAssetController {

    @Autowired
    private CustomAssetSearchFacade customAssetSearchFacade;

    @RequestMapping(path = "/asset/search", method = RequestMethod.GET, produces="image/png" )
    public byte[] searchImages(@RequestParam(value = "query") String query,
                               @RequestParam(value = "save", defaultValue = "true") boolean save) throws ImageSearchException {
        try {
            return customAssetSearchFacade.search(query,save);
        }
        catch (  IOException e) {
            throw new ImageSearchException(e.getMessage(),e.getCause());
        }
    }

    @RequestMapping(path = "/asset/{s3Id:.+}", method = RequestMethod.GET, produces="image/png" )
    public byte[] viewImage(@PathVariable( "s3Id") String s3Id) throws ImageSearchException {
        try {
            return customAssetSearchFacade.view(s3Id);
        }
        catch (  IOException e) {
            throw new ImageSearchException(e.getMessage(),e.getCause());
        }
    }

    @RequestMapping(path = "/asset", method = RequestMethod.GET)
    public Page<CustomAssetView> listCustomAssets(Pageable pageable,HttpServletRequest request) {
            return customAssetSearchFacade.list(pageable, request.getLocalName(), request.getLocalPort());
    }
}
