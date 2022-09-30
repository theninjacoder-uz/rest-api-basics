package com.epam.esm.service.giftCertificate;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GiftCertificateService extends BaseService<GiftCertificateRequestDto, GiftCertificateResponseDto> {

    AppResponseDto<List<GiftCertificateResponseDto>> getList(
            String searchQuery,
            String tagName,
            String sortByDate,
            String sortByName
    );

    AppResponseDto<GiftCertificateResponseDto> update(
        GiftCertificateRequestDto newGiftCertificateRequest,
        Long id
    );
}
