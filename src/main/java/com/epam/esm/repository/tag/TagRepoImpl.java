package com.epam.esm.repository.tag;

import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.mapper.tag.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TagRepoImpl implements TagRepo {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Tag> create(Tag tag) {
        return null;
    }

    @Override
    public Optional<Tag> get(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Tag> getList() {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public Optional<Tag> update(Long id, Tag tag) {
        return Optional.empty();
    }

    //// TODO: 27.09.2022 Question: is it correct with tagRequestDto passed to methods of repository?
    @Override
    public List<Tag> saveTagList(List<TagRequestDto> tagsRequestDto) {

        final String QUERY_SAVE_TAG_LIST = "WITH result AS ( " +
                "INSERT INTO tag(name) VALUES(?) ON CONFLICT (name) DO NOTHING " +
                "RETURNING id, name)\n SELECT * FROM result;";

        return tagsRequestDto.stream().map(requestDto -> jdbcTemplate
                        .queryForObject(QUERY_SAVE_TAG_LIST, Tag.class, requestDto.getName()))
               .collect(Collectors.toList());
    }

    @Override
    public Optional<Tag> findTagByName(String tagName) {
        final String QUERY_GET_TAG_BY_NAME = "SELECT * FROM tag WHERE name = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(QUERY_GET_TAG_BY_NAME, new TagMapper(), tagName));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
