package com.epam.esm.repository.tag;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.BaseRepo;

import java.util.List;
import java.util.Optional;

public interface TagRepo extends BaseRepo<Tag> {
    List<Tag> saveTagList(List<Tag> tags, long id);
    Optional<Tag> findTagByName(String tagName);

    List<Tag> getByGiftId(long id);
}
