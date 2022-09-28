package com.epam.esm.service.tag;

import com.epam.esm.dto.response.BaseResponseDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.service.BaseService;

public interface TagService extends BaseService<TagResponseDto> {
    BaseResponseDto getList();
}
