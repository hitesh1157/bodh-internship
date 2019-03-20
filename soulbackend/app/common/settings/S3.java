package common.settings;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import models.AudioDetail;
import models.VideoDetail;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.gagravarr.opus.tools.OpusInfoTool;
import play.Configuration;
import play.Logger;
import play.mvc.Http;
import utils.Utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshit on 13/03/17.
 */
public class S3 {
    private static String bucketName;
    private static AmazonS3 amazonS3;
    public static String tempUploadDirPath;

    @Inject
    public S3(Configuration configuration) {
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
    }

    /*
     * @Url should only contain youtube link
     */
    public static VideoDetail downloadConvertAndUploadVideo(String url, @Nonnull String filename) {
        Logger.info("downloadConvertAndUploadVideo : url - " + url);
        try {
            filename = filename.trim();
            String sourceFileName = FilenameUtils.getBaseName(filename) + ".m4a";
            String myCommand = "youtube-dl -f 139 -o " + Utils.convertForShell(sourceFileName) + " " + url;
            Logger.info("command to exec : " + myCommand + " , url : " + url);
            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", myCommand}, null, new File(tempUploadDirPath));
            p.waitFor();

//            File videoDestinationFile = new File(tempUploadDirPath + sourceFileName);
//            Http.MultipartFormData.FilePart videoFilepart = new Http.MultipartFormData.FilePart("file", videoDestinationFile.getName(), "video/mp4", videoDestinationFile);
//            Logger.info("Starting upload : " + sourceFileName);
//            String videoLocation = S3.upload(videoFilepart, sourceFileName);
//            Logger.info("video uploaded at : " + videoLocation + " , url : " + url);

            String destFile = tempUploadDirPath + FilenameUtils.getBaseName(filename) + ".opus";
            convertToOpus(tempUploadDirPath + sourceFileName, destFile);
            File audioDestinationFile = new File(destFile);
            String location = S3.uploadFile("file", audioDestinationFile, "audio/opus", FilenameUtils.getBaseName(filename) + ".opus");

            String destMp3File = tempUploadDirPath + FilenameUtils.getBaseName(filename) + ".mp3";
            convertToMp3(tempUploadDirPath + sourceFileName, destMp3File);
            String locationMp3 = S3.uploadFile("file", new File(destMp3File), "audio/mp3", FilenameUtils.getBaseName(filename) + ".mp3");

            Integer duration = OpusInfoTool.getAudioLengthInMills(audioDestinationFile);
            removeFiles(destFile, tempUploadDirPath + sourceFileName, destMp3File);

            return new VideoDetail(location, locationMp3, null, duration);
        } catch (Exception e) {
            Utils.logException(e);
            return null;
        }
    }

    public static AudioDetail downloadConvertAndUpload(String url, @Nonnull String filename) {
        try {
            filename = filename.trim();
            String sourceFileName = "" + System.currentTimeMillis() + "." + FilenameUtils.getExtension(url);
            File sourceFile = new File(tempUploadDirPath + FilenameUtils.getBaseName(sourceFileName));
            FileUtils.copyURLToFile(new URL(url), sourceFile);

            String destFile = tempUploadDirPath + FilenameUtils.getBaseName(sourceFileName) + ".opus";
            convertToOpus(sourceFile.getPath(), destFile);
            File destinationFile = new File(destFile);
            String location = S3.uploadFile("file", destinationFile, "audio/opus", FilenameUtils.getBaseName(filename) + ".opus");

            String destMp3File = tempUploadDirPath + FilenameUtils.getBaseName(sourceFileName) + ".mp3";
            convertToMp3(sourceFile.getPath(), destMp3File);
            String locationMp3 = S3.uploadFile("file", new File(destMp3File), "audio/mp3", FilenameUtils.getBaseName(filename) + ".mp3");
            Integer duration = OpusInfoTool.getAudioLengthInMills(destinationFile);
            removeFiles(destFile, tempUploadDirPath + sourceFileName, destMp3File);

            return new AudioDetail(location, locationMp3, duration);
        } catch (Exception e) {
            Utils.logException(e);
            return null;
        }
    }

    public static AudioDetail convertAndUpload(Http.MultipartFormData.FilePart filepart, @Nonnull String filename) {
        try {
            filename = filename.trim();
            Path path = new File(tempUploadDirPath + filepart.getFilename()).toPath();
            Files.copy(new FileInputStream((File) filepart.getFile()), path, StandardCopyOption.REPLACE_EXISTING);

            String destFileName = tempUploadDirPath + FilenameUtils.getBaseName(filepart.getFilename()) + ".opus";
            convertToOpus(path.toString(), destFileName);
            File destFile = new File(destFileName);
            String location = uploadFile(filepart.getKey(), destFile, "audio/opus", FilenameUtils.getBaseName(filename) + ".opus");


            String destMp3FileName = tempUploadDirPath + FilenameUtils.getBaseName(filepart.getFilename()) + ".mp3";
            convertToMp3(path.toString(), destFileName);
            String locationMp3 = uploadFile(filepart.getKey(), new File(destMp3FileName), "audio/mp3", FilenameUtils.getBaseName(filename) + ".mp3");

            Integer duration = OpusInfoTool.getAudioLengthInMills(destFile);
            removeFiles(destFileName, path.toString(), destFileName);

            return new AudioDetail(location, locationMp3, duration);
        } catch (Exception e) {
            Utils.logException(e);
            return null;
        }
    }

    public static void removeFiles(String... paths) {
        try {
            String myCommand = "rm";
            for (String path : paths) {
                myCommand += " " + Utils.convertForShell(path);
            }
            Logger.info("command to exec : " + myCommand);
            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", myCommand});
            p.waitFor();
        } catch (Exception e) {
            Utils.logException(e);
        }
    }

    public static void convertToMp3(String sourcePath, String destPath) {
        try {
            if (FilenameUtils.getExtension(sourcePath).equalsIgnoreCase("mp3")) {
                FileUtils.copyFile(new File(sourcePath), new File(destPath));
            } else {
                String myCommand = "avconv -i " + Utils.convertForShell(sourcePath) + " " + Utils.convertForShell(destPath);
                Logger.info("command to exec : " + myCommand);
                Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", myCommand});
                p.waitFor();
            }
        } catch (Exception e) {
            Utils.logException(e);
        }
    }

    public static void convertToOpus(String sourcePath, String destPath) {
        try {
            String myCommand = "avconv -i " + Utils.convertForShell(sourcePath) + " -f wav - | opusenc --bitrate 48 - " + Utils.convertForShell(destPath);
            Logger.info("command to exec : " + myCommand);
            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", myCommand});
            p.waitFor();
        } catch (Exception e) {
            Utils.logException(e);
        }
    }

    public static String uploadFile(String key, File file, String contentType, @Nullable String name) {
        Http.MultipartFormData.FilePart filepart = new Http.MultipartFormData.FilePart(key, file.getName(), contentType, file);
        return upload(filepart, name);
    }

    public static String upload(Http.MultipartFormData.FilePart filePart, @Nullable String name) {
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
            amazonS3.abortMultipartUpload(new AbortMultipartUploadRequest(
                    bucketName, key, initResponse.getUploadId()));
            Utils.logException(e);
            return null;
        }
    }
}
