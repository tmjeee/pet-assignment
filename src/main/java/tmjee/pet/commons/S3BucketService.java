package tmjee.pet.commons;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import tmjee.pet.config.PetAppConfigProperties;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.UUID;

public class S3BucketService {

    public static class SavedDog {
        public final LocalDateTime date;
        public final String location;
        public final String key;
        SavedDog(LocalDateTime date, String location, String key) {
            this.date= date;
            this.location = location;
            this.key = key;
        }
    }

    public static class DeletedDog {
        public final String key;
        DeletedDog(String key) {
            this.key = key;
        }
    }


    @Autowired
    private PetAppConfigProperties configProperties;


    private AWSCredentials credentials;
    private AmazonS3 s3;

    @PostConstruct
    public void init() {
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(configProperties.getAws().getBucketRegion())
                .build();
    }


    public DeletedDog deleteDogPicture(String s3Key) throws AmazonServiceException, AmazonClientException {
        String bucketName = configProperties.getAws().getBucketName();

        s3.deleteObject(bucketName, s3Key);

        return  new DeletedDog(s3Key);
    }

    public SavedDog saveDogPicture(byte[] pictureInBytes) throws AmazonServiceException, AmazonClientException {

        String bucketName = configProperties.getAws().getBucketName();
        String key = String.format("DogPicture-%s", UUID.randomUUID().toString());

        ByteArrayInputStream baos = new ByteArrayInputStream(pictureInBytes);
        PutObjectResult r = s3.putObject(bucketName, key, baos, new ObjectMetadata());
        String url = s3.getUrl(bucketName, key).toExternalForm();

        return new SavedDog(LocalDateTime.now(), url, key);
    }
}
