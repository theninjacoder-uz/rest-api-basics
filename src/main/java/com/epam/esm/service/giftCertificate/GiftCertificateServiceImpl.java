package com.epam.esm.service.giftCertificate;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.BaseResponseDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.mapper.giftCertificate.GifCertificateModelMapperImpl;
import com.epam.esm.repository.giftCertificate.GiftCertificateRepo;
import com.epam.esm.repository.tag.TagRepoImpl;
import com.epam.esm.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

//    private final ModelMapper modelMapper;
    private final GiftCertificateRepo giftCertificateRepo;
    private final GifCertificateModelMapperImpl modelMapper;
    private final TagRepoImpl tagRepo;

    @Override
    public BaseResponseDto create(GiftCertificateRequestDto requestDto) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepo.create(modelMapper.toEntity(requestDto));
        if (giftCertificate.isEmpty()) {
//            return BaseResponseDto.errorResponse(HttpStatus.CONFLICT.value(), "Gift certificate creation error", 0);
            return new BaseResponseDto(HttpStatus.CONFLICT.value(), "Gift certificate creation error", 0);
        }
//        return new BaseResponseDto.successResponse(HttpStatus.OK.value(),
        return new BaseResponseDto(HttpStatus.OK.value(),
                "Gift certificate created!",
                modelMapper.toDto(giftCertificate.get(), tagRepo.saveTagList(requestDto.getTags())));
    }

    @Override
    public BaseResponseDto get(Long id) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponseDto delete(Long id) {
        return null;
    }

    @Override
    public BaseService<List<GiftCertificateRequestDto>> getList(String searchQuery, String tagName, String sortByDate, String sortByName) {
        return null;
    }

    @Override
    @Transactional
    public BaseService<GiftCertificateRequestDto> update(GiftCertificateRequestDto newGiftCertificateRequest, Long id) {
        return null;
    }
}
