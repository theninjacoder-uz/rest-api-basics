package com.epam.esm.model.mapper.giftCertificate;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;

import java.util.List;

public interface GiftCertificateModelMapper {
    GiftCertificateResponseDto toDto(GiftCertificate giftCertificate, List<Tag> tagList);
    GiftCertificate toEntity(GiftCertificateRequestDto requestDto);
}
