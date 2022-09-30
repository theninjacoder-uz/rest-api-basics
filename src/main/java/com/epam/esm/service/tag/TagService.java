package com.epam.esm.service.tag;

import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.BaseService;

import java.util.List;

public interface TagService extends BaseService<TagRequestDto, Tag> {
    AppResponseDto<List<Tag>> getList();
    List<Tag> saveTagListAndLinkTables(List<TagRequestDto> requestDtoList, long giftCertificateId);

    List<Tag> getTagListByGiftId(long giftId);
}
