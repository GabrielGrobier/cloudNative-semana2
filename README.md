# Mini S3 Spring Boot Example

This project demonstrates a minimal Spring Boot application that connects to Amazon S3 using the AWS SDK v2.
It exposes a REST API with three endpoints:

* `POST /api/files/upload` – Upload a file via multipart form data.
* `GET /api/files/list` – List all files stored under the `uploads/` prefix in the S3 bucket.
* `GET /api/files/download/{name}` – Download a file by its name from the bucket.

## Prerequisites

* Java 17 or later
* Maven 3.6+
* An S3 bucket in your AWS account
* AWS credentials with permissions to access the S3 bucket

## Setup

1. Clone or download this repository.
2. Open `src/main/resources/application.properties` and set:
   * `aws.region` – your AWS region (e.g. `us-east-1`).
   * `aws.s3.bucket` – the name of your bucket.
   * The AWS credentials fields (`aws.accessKeyId`, `aws.secretAccessKey`, `aws.sessionToken`) are placeholders that should be provided via environment variables. When running locally you can set `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY` and `AWS_SESSION_TOKEN` in your shell.

3. Compile and run the application:

```bash
mvn clean spring-boot:run
```

## Testing the API

Use `curl` or any HTTP client:

**Upload a file:**
```bash
curl -F "file=@/path/to/myfile.pdf" http://localhost:8080/api/files/upload
```

**List files:**
```bash
curl http://localhost:8080/api/files/list
```

**Download a file:**
```bash
curl -O http://localhost:8080/api/files/download/myfile.pdf
```

## Notes

* This sample uses session-based credentials for demonstration purposes. For production applications, consider using IAM roles or AWS profiles.
* The `uploads/` prefix organizes objects within the bucket; you can change this in `S3Service`.