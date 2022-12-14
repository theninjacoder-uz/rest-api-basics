package com.epam.esm.model.mapper.giftCertificate;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GifCertificateModelMapperImpl implements GiftCertificateModelMapper {
    @Override
    public GiftCertificateResponseDto toDto(GiftCertificate certificate, List<Tag> tagList) {
        GiftCertificateResponseDto responseDto = new GiftCertificateResponseDto();
        responseDto.setId(certificate.getId());
        responseDto.setName(certificate.getName());
        responseDto.setPrice(certificate.getPrice());
        responseDto.setDuration(certificate.getDuration());
        responseDto.setDescription(certificate.getDescription());
        responseDto.setCreateDate(certificate.getCreateDate());
        responseDto.setLastUpdatedDate(certificate.getLastUpdatedDate());
        responseDto.setTags(tagList);
        return responseDto;
    }

    @Override
    public GiftCertificate toEntity(GiftCertificateRequestDto requestDto) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(requestDto.getName());
        giftCertificate.setPrice(requestDto.getPrice());
        giftCertificate.setDuration(requestDto.getDuration());
        giftCertificate.setDescription(requestDto.getDescription());
        return giftCertificate;
    }

    @Override
    public GiftCertificate map(GiftCertificateRequestDto requestDto, GiftCertificate certificate) {
        certificate.setName(requestDto.getName() != null ? requestDto.getName() : certificate.getName());
        certificate.setDescription(requestDto.getDescription() != null ? requestDto.getDescription() : certificate.getDescription());
        certificate.setDuration(requestDto.getDuration() != null ? requestDto.getDuration() : certificate.getDuration());
        certificate.setPrice(requestDto.getPrice() != null ? requestDto.getPrice() : certificate.getPrice());
        return certificate;
    }
}
