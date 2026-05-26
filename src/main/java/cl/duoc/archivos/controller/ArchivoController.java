package cl.duoc.archivos.controller;

import cl.duoc.archivos.service.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller exposing endpoints to upload, list and download files via HTTP.
 */
@RestController
@RequestMapping("/api/files")
public class ArchivoController {

    private final S3Service s3Service;

    public ArchivoController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * Uploads a file to S3.
     *
     * Example call with curl:
     * curl -F "file=@/path/to/file.pdf" http://localhost:8080/api/files/upload
     *
     * @param file multipart file sent in the request
     * @return a message indicating success
     * @throws Exception if upload fails
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String response = s3Service.uploadFile(file);
        return ResponseEntity.ok(response);
    }

    /**
     * Lists all files stored under the "uploads/" prefix in the configured bucket.
     *
     * @return list of keys
     */
    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles() {
        return ResponseEntity.ok(s3Service.listFiles());
    }

    /**
     * Downloads a file by its name from the "uploads/" prefix.
     *
     * Example call:
     * curl -O http://localhost:8080/api/files/download/file.pdf
     *
     * @param name the file name
     * @return a ResponseEntity with the file content
     */
    @GetMapping("/download/{name}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String name) {
        byte[] fileBytes = s3Service.downloadFile(name);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileBytes);
    }
}