package org.palpalmans.ollive_back.domain.image.repository;

import org.palpalmans.ollive_back.domain.image.model.ImageType;
import org.palpalmans.ollive_back.domain.image.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findALlByImageTypeAndReferenceId(ImageType imageType, Long referenceId);
}
