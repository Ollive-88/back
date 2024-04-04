package org.palpalmans.ollive_back.domain.image.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final MinioClient minioClient;

    @Value("${minio.url}")
    private String URL;

    @Value("${minio.bucket.name}")
    private String BUCKET_NAME;

    @Value("${minio.dir.image}")
    private String IMAGE_DIR;

    private final String PATH = URL + "/" + BUCKET_NAME;

    public String saveImageFile(MultipartFile multipartFile)
            throws IOException, ServerException, InsufficientDataException,
            ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {

        // validateBucket
        boolean isExist = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(BUCKET_NAME)
                        .build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(BUCKET_NAME)
                    .build());
        }

        // save
        String fileName = IMAGE_DIR + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(fileName)
                .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                .contentType(multipartFile.getContentType())
                .build()
        );
        return PATH + fileName;
    }
}
