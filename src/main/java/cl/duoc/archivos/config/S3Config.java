package cl.duoc.archivos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Configuration class for the AWS S3 client.
 *
 * This class builds an S3Client using session credentials. The credentials and
 * region are loaded from the application.properties file or environment variables.
 */
@Configuration
public class S3Config {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.sessionToken}")
    private String sessionToken;

    /**
     * Creates an S3Client bean that can be injected into services.
     *
     * @return configured S3Client
     */
    @Bean
    public S3Client s3Client() {
        AwsSessionCredentials credentials = AwsSessionCredentials.create(
                accessKeyId,
                secretAccessKey,
                sessionToken
        );

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}