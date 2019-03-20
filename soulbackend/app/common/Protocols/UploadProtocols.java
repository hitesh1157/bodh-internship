package common.Protocols;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FilenameUtils;
import play.Configuration;
import play.Logger;
import play.mvc.Http;
import utils.Utils;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UploadProtocols {

    public static String UploadFile(String key, File file, String contentType, @Nullable String name, Configuration configuration) {
        Http.MultipartFormData.FilePart filepart = new Http.MultipartFormData.FilePart(key, file.getName(), contentType, file);
        String uploadFileLocation = UploadFilepart(filepart, name, configuration);
        return uploadFileLocation;
    }

    public static String UploadFilepart(Http.MultipartFormData.FilePart filePart, @Nullable String name, Configuration configuration) {

        String bucketName;
        AmazonS3 amazonS3;
        String tempUploadDirPath;

        bucketName = configuration.getString("s3.bucket");
        AWSCredentials awsCredentials = new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return configuration.getString("aws.accessKeyId");
            }

            @Override
            public String getAWSSecretKey() {
                return configuration.getString("aws.secretKey");
            }
        };
        AWSCredentialsProvider awsCredentialsProvider = new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return awsCredentials;
            }

            @Override
            public void refresh() {

            }
        };
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion(configuration.getString("s3.region"));
        amazonS3 = builder.build();
        tempUploadDirPath = configuration.getString("tempUploadDirPath");

        if (name == null)
            name = filePart.getFilename();
        String key = FilenameUtils.getBaseName(name) + "_" + System.currentTimeMillis();
        if (!FilenameUtils.getExtension(name).isEmpty())
            key = key + "." + FilenameUtils.getExtension(name);
        else if (!FilenameUtils.getExtension(filePart.getFilename()).isEmpty())
            key = key + "." + FilenameUtils.getExtension(filePart.getFilename());

        InitiateMultipartUploadResult initResponse = amazonS3.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, key));
        List<PartETag> partETags = new ArrayList<>();
        File file = (File) filePart.getFile();
        long contentLength = file.length();
        Logger.info(String.valueOf(contentLength));
        long partSize = 5 * 1024 * 1024; // Set part size to 5 MB.
        try {
            // Step 2: Upload parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Last part can be less than 5 MB. Adjust part size.
                partSize = Math.min(partSize, (contentLength - filePosition));

                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucketName)
                        .withKey(key)
                        .withUploadId(initResponse.getUploadId())
                        .withPartNumber(i)
                        .withFileOffset(filePosition)
                        .withFile(file)
                        .withPartSize(partSize);

                // Upload part and add response to our list.
                partETags.add(amazonS3.uploadPart(uploadRequest).getPartETag());

                filePosition += partSize;
            }

            // Step 3: Complete.
            CompleteMultipartUploadRequest compRequest = new
                    CompleteMultipartUploadRequest(bucketName,
                    key,
                    initResponse.getUploadId(),
                    partETags);

            CompleteMultipartUploadResult completeMultipartUploadResult = amazonS3.completeMultipartUpload(compRequest);

            return completeMultipartUploadResult.getLocation();
        } catch (Exception e) {
             // For testing purposes
            amazonS3.abortMultipartUpload(new AbortMultipartUploadRequest(
                    bucketName, key, initResponse.getUploadId()));
            Utils.logException(e);
            return null;
        }
    }
    }
