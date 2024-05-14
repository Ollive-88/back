package org.palpalmans.ollive_back.domain.image.service;

import io.minio.errors.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.palpalmans.ollive_back.domain.image.model.ImageType;
import org.palpalmans.ollive_back.domain.image.model.dto.GetImageResponse;
import org.palpalmans.ollive_back.domain.image.model.entity.Image;
import org.palpalmans.ollive_back.domain.image.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.palpalmans.ollive_back.common.error.ErrorMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageFileService imageFileService;
    private final ImageRepository imageRepository;

    @Transactional
    public void saveImage(List<MultipartFile> images, ImageType imageType, Long referenceId) {
        images.forEach(image -> {
            try {
                String address = imageFileService.saveImageFile(image);
                imageRepository.save(new Image(address, imageType, referenceId));
            } catch (ServerException | InsufficientDataException | ErrorResponseException
                     | IOException | NoSuchAlgorithmException | InvalidKeyException
                     | InvalidResponseException | XmlParserException | InternalException e) {
                log.error(IMAGE_FILE_NOT_SAVED.getMessage());
                log.error("IMAGE_FILE_NOT_SAVED exception", e);
            } catch (PersistenceException e) {
                log.error(IMAGE_ENTITY_NOT_SAVED.getMessage());
                log.error("IMAGE_ENTITY_NOT_SAVED exception", e);
            } catch (Exception e) {
                log.error(UNKNOWN.getMessage());
            }
        });
    }

    @Transactional
    public List<GetImageResponse> getImages(ImageType imageType, Long referenceId) {
        return imageRepository.findALlByImageTypeAndReferenceId(imageType, referenceId)
                .stream()
                .map(image -> new GetImageResponse(
                        image.getId(),
                        image.getAddress(),
                        image.getImageType(),
                        image.getReferenceId()))
                .toList();
    }

    @Transactional
    public void deleteImages(List<Long> images) {
        images.forEach(imageId -> {
            try {
                Image foundImage = imageRepository.findById(imageId)
                        .orElseThrow(EntityNotFoundException::new);
                imageFileService.deleteImage(foundImage.getAddress());
                imageRepository.delete(foundImage);
            } catch (ServerException | InsufficientDataException | ErrorResponseException
                     | IOException | NoSuchAlgorithmException | InvalidKeyException
                     | InvalidResponseException | XmlParserException | InternalException e) {
                log.error(IMAGE_FILE_NOT_DELETED.getMessage());
                log.error("IMAGE_FILE_NOT_DELETED exception", e);
            } catch (PersistenceException e) {
                log.error(IMAGE_ENTITY_NOT_DELETED.getMessage());
                log.error("IMAGE_ENTITY_NOT_SAVED exception", e);
            } catch (Exception e) {
                log.error(UNKNOWN.getMessage());
            }
        });

    }
}
