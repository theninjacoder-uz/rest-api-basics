package com.epam.esm.controller;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.service.giftCertificate.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gift_certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto> create(
            @RequestBody GiftCertificateRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(giftCertificateService.create(requestDto));
    }

    @GetMapping(value = "/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto> get(@PathVariable Long id){
        return ResponseEntity.ok(giftCertificateService.get(id));
    }

    @GetMapping(consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto> getList(
        @RequestParam(required = false, name = "search_term") String searchTerm,
        @RequestParam(required = false, name = "tag_name") String tagName,
        @RequestParam(required = false, name = "sort_date") String sortByDate,
        @RequestParam(required = false, name = "sort_name") String sortByName
    ){
        return ResponseEntity.ok(giftCertificateService.getList(searchTerm, tagName, sortByDate, sortByName));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto> getList(@PathVariable Long id, @RequestBody GiftCertificateRequestDto requestDto){
        return ResponseEntity.ok(giftCertificateService.update(requestDto, id));
    }

    @DeleteMapping(value = "/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto> delete(@PathVariable Long id){
        return ResponseEntity.ok(giftCertificateService.delete(id));
    }


}
