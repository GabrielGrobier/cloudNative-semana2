package cl.duoc.archivos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

/**
 * Service that encapsulates interactions with AWS S3.
 *
 * Provides methods to upload a file, list files and download a file
 * from a specific prefix within a bucket.
 */
@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    /**
     * Constructor injection for the S3Client.
     *
     * @param s3Client configured S3Client
     */
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Uploads a file to S3 under the "uploads/" prefix. Uses the original filename.
     *
     * @param file uploaded file
     * @return message indicating successful upload and key name
     * @throws IOException if reading file fails
     */
    public String uploadFile(MultipartFile file) throws IOException {
        String key = "uploads/" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(file.getBytes())
        );

        return "Archivo subido exitosamente: " + key;
    }

    /**
     * Lists the files inside the "uploads/" prefix.
     *
     * @return list of object keys
     */
    public List<String> listFiles() {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix("uploads/")
                .build();

        return s3Client.listObjectsV2(request)
                .contents()
                .stream()
                .map(S3Object::key)
                .toList();
    }

    /**
     * Downloads a file from the "uploads/" prefix by name.
     *
     * @param fileName name of the file within the uploads prefix
     * @return byte array representing the file content
     */
    public byte[] downloadFile(String fileName) {
        String key = "uploads/" + fileName;

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(request);

        return objectBytes.asByteArray();
    }
}