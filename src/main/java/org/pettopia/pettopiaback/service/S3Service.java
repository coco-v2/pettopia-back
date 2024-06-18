package org.pettopia.pettopiaback.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.credentials.bucket-name}")
    public String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;


    //Pre-Signed URL 받아옴
    public String getPreSignedUrl(String fileName) {
        log.info("get presinged url");

        if (amazonS3.doesObjectExist(bucketName, fileName)) {
            amazonS3.deleteObject(bucketName, fileName);
            log.info("Deleted existing file with name: {}", fileName);
        }

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(fileName);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        log.info(expiration.toString());
        return expiration;
    }


    public void fileDelete(String fileUrl) {

        String fileKey = fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length() + 1);

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
        s3.deleteObject(bucketName, fileKey);

        System.out.println(String.format("[%s] deletion complete", fileKey));
    }


}
