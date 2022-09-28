package com.epam.esm.service;

import com.epam.esm.dto.response.BaseResponseDto;

public interface BaseService<T> {

    BaseResponseDto create(T type);

    BaseResponseDto get(Long id);

    BaseResponseDto delete(Long id);

}
