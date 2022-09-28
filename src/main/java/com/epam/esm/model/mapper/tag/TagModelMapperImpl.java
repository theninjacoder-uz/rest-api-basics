package com.epam.esm.model.mapper.tag;

import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagModelMapperImpl implements TagModelMapper{
    @Override
    public TagResponseDto toDto(Tag tag) {
        TagResponseDto responseDto =  new TagResponseDto();
        responseDto.setId(tag.getId());
        responseDto.setName(tag.getName());
        return responseDto;
    }

    @Override
    public Tag toEntity(TagRequestDto tagRequest) {
        Tag tag = new Tag();
        tag.setName(tagRequest.getName());
        return tag;
    }
}
