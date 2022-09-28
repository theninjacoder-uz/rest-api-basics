package com.epam.esm.repository.tag;

import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.BaseRepo;

import java.util.List;
import java.util.Optional;

public interface TagRepo extends BaseRepo<Tag> {
    List<Tag> saveTagList(List<TagRequestDto> tags);
    Optional<Tag> findTagByName(String tagName);
}
