package com.linguo.customasset.dao;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by bin on 17/09/2016.
 */

@Service
public class AwsS3Dao {
    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public String upload(byte[] image, String searchName) {
        String fileKey = searchName.toLowerCase().replace(" ","_")+".png";
        upload(new ByteArrayInputStream(image), fileKey, image.length);
        return fileKey;
    }

    private PutObjectResult upload(InputStream inputStream, String uploadKey, int length) {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(length);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadKey, inputStream, metadata);

        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

        PutObjectResult putObjectResult = amazonS3Client.putObject(putObjectRequest);


        IOUtils.closeQuietly(inputStream);

        return putObjectResult;
    }

//    public List<PutObjectResult> upload(MultipartFile[] multipartFiles) {
//        List<PutObjectResult> putObjectResults = new ArrayList<>();
//
//        Arrays.stream(multipartFiles)
//                .filter(multipartFile -> !StringUtils.isEmpty(multipartFile.getOriginalFilename()))
//                .forEach(multipartFile -> {
//                    try {
//                        putObjectResults.add(upload(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), image.length));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//
//        return putObjectResults;
//    }

    public byte[] download(String key) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);

        S3Object s3Object = amazonS3Client.getObject(getObjectRequest);

        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

        try {
            byte[] bytes = IOUtils.toByteArray(objectInputStream);
            return bytes;
        }finally {
            objectInputStream.close();
        }
    }

    public void delete(String key) throws IOException {

         amazonS3Client.deleteObject(bucket, key);

    }

    public List<S3ObjectSummary> list() {
        ObjectListing objectListing = amazonS3Client.listObjects(new ListObjectsRequest().withBucketName(bucket));

        List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

        return s3ObjectSummaries;
    }


}