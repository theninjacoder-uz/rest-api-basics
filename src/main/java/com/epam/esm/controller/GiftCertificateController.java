package com.epam.esm.controller;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.BaseResponseDto;
import com.epam.esm.service.giftCertificate.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gift_certificate")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @RequestMapping(value ="/add",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(
            @RequestBody GiftCertificateRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(giftCertificateService.create(requestDto));
    }

    @GetMapping(consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> get(){
        return ResponseEntity.ok(new BaseResponseDto(HttpStatus.OK.value(), "gift certificate", "data"));
    }

}
