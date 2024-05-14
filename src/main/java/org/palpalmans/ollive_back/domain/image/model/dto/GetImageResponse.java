package org.palpalmans.ollive_back.domain.image.model.dto;

import org.palpalmans.ollive_back.domain.image.model.ImageType;

public record GetImageResponse(
        Long id,
        String address,
        ImageType imageType,
        Long referenceId
) {
}
