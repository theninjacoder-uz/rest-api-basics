package com.epam.esm.model.mapper.tag;

import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.model.entity.Tag;

public interface TagModelMapper {
    TagResponseDto toDto(Tag tag);
    Tag toEntity(TagRequestDto tagRequest);
}
