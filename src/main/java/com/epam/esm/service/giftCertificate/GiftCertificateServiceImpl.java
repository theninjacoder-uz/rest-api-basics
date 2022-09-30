package com.epam.esm.service.giftCertificate;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.exception.DataPersistenceException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.mapper.giftCertificate.GiftCertificateModelMapper;
import com.epam.esm.repository.giftCertificate.GiftCertificateRepo;
import com.epam.esm.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    //    private final ModelMapper modelMapper;
    private final TagService tagService;
    private final GiftCertificateRepo giftCertificateRepo;
    private final GiftCertificateModelMapper modelMapper;

    @Override
    @Transactional
    public AppResponseDto<GiftCertificateResponseDto> create(GiftCertificateRequestDto requestDto) {

        try {
            final GiftCertificate certificate = modelMapper.toEntity(requestDto);
            certificate.setCreateDate(LocalDateTime.now());
            certificate.setLastUpdatedDate(LocalDateTime.now());

            final GiftCertificate savedCertificate = giftCertificateRepo.create(certificate);

            return new AppResponseDto<>(HttpStatus.OK.value(),
                    "Gift certificate created!",
                    modelMapper.toDto(savedCertificate,
                            tagService.saveTagListAndLinkTables(requestDto.getTags(), savedCertificate.getId())));

        } catch (DataAccessException e) {
            throw new DataPersistenceException("Error when gift certificate creation: " + e.getMessage());
        }
    }

    @Override
    public AppResponseDto<GiftCertificateResponseDto> get(Long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateRepo.get(id);
        if (giftCertificate.isPresent()) {
            return new AppResponseDto<>(HttpStatus.OK.value(), "gift certificate",
                    modelMapper.toDto(giftCertificate.get(), tagService.getTagListByGiftId(id)));
        }
        throw new ResourceNotFoundException(id);
    }

    @Override
    @Transactional
    public AppResponseDto<Boolean> delete(Long id) {
        if (giftCertificateRepo.delete(id)) {
            return new AppResponseDto<>(HttpStatus.OK.value(), "gift certificate deleted", true);
        }
        throw new ResourceNotFoundException(id);
    }

    @Override
    public AppResponseDto<List<GiftCertificateResponseDto>> getList(String searchQuery, String tagName, String sortByDate, String sortByName) {

        List<GiftCertificate> certificateList = giftCertificateRepo
                .findByParametersAndSort(searchQuery, tagName, sortByDate, sortByName);

        return new AppResponseDto<>(
                HttpStatus.OK.value(),
                "search result",
                certificateList.stream()
                        .map(giftCertificate ->
                                modelMapper.toDto(giftCertificate, tagService.getTagListByGiftId(giftCertificate.getId()))
                        ).collect(Collectors.toList())
        );
    }

    @Override
    @Transactional
    public AppResponseDto<GiftCertificateResponseDto> update(GiftCertificateRequestDto requestDto, Long id) {

        Optional<GiftCertificate> oldCertificate = giftCertificateRepo.get(id);
        if (oldCertificate.isEmpty()) {
            throw new ResourceNotFoundException(id);
        }
        GiftCertificate giftCertificate = modelMapper.map(requestDto, oldCertificate.get());
        giftCertificate.setLastUpdatedDate(LocalDateTime.now());

        Optional<GiftCertificate> certificateOptional = giftCertificateRepo.update(id, giftCertificate);
        if (certificateOptional.isPresent()) {
            return new AppResponseDto<>(
                    HttpStatus.OK.value(),
                    "Certificate updated successfully",
                    modelMapper.toDto(certificateOptional.get(), tagService.getTagListByGiftId(id)));
        }
        throw new DataPersistenceException("Error in updating certificate");
    }
}
