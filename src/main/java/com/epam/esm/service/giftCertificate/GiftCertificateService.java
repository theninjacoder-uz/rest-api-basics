package com.epam.esm.service.giftCertificate;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GiftCertificateService extends BaseService<GiftCertificateRequestDto> {

    BaseService<List<GiftCertificateRequestDto>> getList(
            String searchQuery,
            String tagName,
            String sortByDate,
            String sortByName
    );

    BaseService<GiftCertificateRequestDto> update(
        GiftCertificateRequestDto newGiftCertificateRequest,
        Long id
    );
}
