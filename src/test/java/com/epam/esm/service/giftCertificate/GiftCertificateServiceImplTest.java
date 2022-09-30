package com.epam.esm.service.giftCertificate;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.mapper.giftCertificate.GiftCertificateModelMapper;
import com.epam.esm.repository.giftCertificate.GiftCertificateRepo;
import com.epam.esm.service.tag.TagService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepo giftCertificateRepo;

    @Mock
    private TagService tagService;

    @Mock
    private GiftCertificateModelMapper modelMapper;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;


    @Test
    public void createGiftCertificate() {

        GiftCertificateRequestDto requestDto1 =
                new GiftCertificateRequestDto(
                        "name", "description", BigDecimal.valueOf(100), 10, null);

        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1L);
        certificate.setName("name");
        certificate.setDescription("description");
        certificate.setPrice(BigDecimal.valueOf(100));
        certificate.setDuration(10);

        GiftCertificateRequestDto requestDto2 = new GiftCertificateRequestDto();
        requestDto2.setPrice(BigDecimal.valueOf(-20));

        when(modelMapper.toEntity(requestDto1)).thenReturn(certificate);
        when(giftCertificateRepo.create(certificate)).thenReturn(certificate);
        when(tagService.saveTagListAndLinkTables(null, 1L)).thenReturn(null);
        when(modelMapper.toDto(certificate, null)).thenReturn(new GiftCertificateResponseDto());
        when(modelMapper.toEntity(requestDto2)).thenThrow(ConstraintViolationException.class);

        AppResponseDto<GiftCertificateResponseDto> response = giftCertificateService.create(requestDto1);

        assertEquals(HttpStatus.OK.value(), response.getHttpStatus());
        assertEquals("Gift certificate created!", response.getMessage());
        assertThrows(ConstraintViolationException.class, () -> giftCertificateService.create(requestDto2));
    }

    @Test
    void getGiftCertificateById() {
        long id = 1L;
        GiftCertificate giftCertificate =
                new GiftCertificate(
                        id, "name", "description", BigDecimal.valueOf(10), 10, null, null);
        List<Tag> tagList = List.of(new Tag(1L, "testTag"));
        GiftCertificateResponseDto responseDto = new GiftCertificateResponseDto();
        given(giftCertificateRepo.get(id)).willReturn(java.util.Optional.of(giftCertificate));
        given(tagService.getTagListByGiftId(id)).willReturn(tagList);
        given(modelMapper.toDto(giftCertificate, tagList)).willReturn(responseDto);

        AppResponseDto<GiftCertificateResponseDto> actual = giftCertificateService.get(id);

        assertEquals(HttpStatus.OK.value(), actual.getHttpStatus());
        assertEquals("gift certificate", actual.getMessage());
        assertEquals(responseDto, actual.getData());
//        assertThrows(tagList.get(0), actual.getData())
        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.get(-1L));
    }

    @Test
    void deleteGiftCertificate() {
        long id = 1L;
        long id1 = 10L;
        when(giftCertificateRepo.delete(id1)).thenReturn(false);
        when(giftCertificateRepo.delete(id)).thenReturn(true);

        AppResponseDto<Boolean> responseDto = giftCertificateService.delete(id);

        assertEquals(HttpStatus.OK.value(), responseDto.getHttpStatus());
        assertEquals("gift certificate deleted", responseDto.getMessage());
        assertTrue((Boolean) responseDto.getData());
        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.delete(id1));
    }

    @Test
    void getList() {
        String searchQuery = "";
        String tagName = "new";
        String sortByDate = "ASC";
        String sortByName = "";

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(new BigDecimal(100));
        giftCertificate.setDuration(10);

        List<GiftCertificate> certificateList = List.of(giftCertificate);

        given(
                giftCertificateRepo.findByParametersAndSort(
                        searchQuery, tagName, sortByDate, sortByName))
                .willReturn(certificateList);

        GiftCertificateResponseDto responseDto = new GiftCertificateResponseDto();
        responseDto.setId(1L);
        responseDto.setName("name");
        responseDto.setDescription("description");
        responseDto.setPrice(new BigDecimal(100));
        responseDto.setDuration(10);

        given(modelMapper.toDto(giftCertificate, List.of())).willReturn(responseDto);

        AppResponseDto<List<GiftCertificateResponseDto>> result1 =
                giftCertificateService.getList(searchQuery, tagName, sortByDate, sortByName);

        assertEquals(HttpStatus.OK.value(), result1.getHttpStatus());
        assertEquals("search result", result1.getMessage());
    }

    @Test
    void updateWhenCertificateIsNotFoundThenThrowException() {
        long id = 1L;
        GiftCertificateRequestDto requestDto = new GiftCertificateRequestDto();

        given(giftCertificateRepo.get(id)).willReturn(java.util.Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> giftCertificateService.update(requestDto, id));
    }

    @Test
    void updateWhenCertificateIsFoundThenReturnUpdatedCertificate() {
        GiftCertificateRequestDto requestDto = new GiftCertificateRequestDto();
        requestDto.setName("name");
        requestDto.setDescription("description");
        requestDto.setPrice(BigDecimal.valueOf(100));
        requestDto.setDuration(10);

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("name");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(BigDecimal.valueOf(100));
        giftCertificate.setDuration(10);

        GiftCertificateResponseDto responseDto = new GiftCertificateResponseDto();
        responseDto.setId(1L);
        responseDto.setName("name");
        responseDto.setDescription("description");
        responseDto.setPrice(BigDecimal.valueOf(100));
        responseDto.setDuration(10);

        given(giftCertificateRepo.get(1L))
                .willReturn(java.util.Optional.of(giftCertificate));
        given(modelMapper.map(requestDto, giftCertificate)).willReturn(giftCertificate);
        given(giftCertificateRepo.update(1L, giftCertificate))
                .willReturn(java.util.Optional.of(giftCertificate));
        given(tagService.getTagListByGiftId(1L)).willReturn(List.of());
        given(modelMapper.toDto(giftCertificate, List.of())).willReturn(responseDto);

        AppResponseDto<GiftCertificateResponseDto> actual = giftCertificateService.update(requestDto, 1L);

        assertEquals(HttpStatus.OK.value(), actual.getHttpStatus());
        assertEquals("Certificate updated successfully", actual.getMessage());
        assertEquals(responseDto, actual.getData());
    }

}