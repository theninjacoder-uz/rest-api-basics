package com.epam.esm.repository.tag;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.mapper.tag.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public Tag create(Tag tag) {
        final String QUERY_SAVE_TAG = "WITH result AS (INSERT INTO tag(name) VALUES(?) RETURNING id, name) " +
                "SELECT * FROM result;";
        return jdbcTemplate.queryForObject(QUERY_SAVE_TAG, new TagMapper(), tag.getName());
    }

    @Override
    public Optional<Tag> get(Long id) {
        final String QUERY_FIND_TAG_BY_ID = "SELECT * FROM tag WHERE id = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(QUERY_FIND_TAG_BY_ID, new TagMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> getList() {
        final String QUERY_FIND_ALL_TAGS = "SELECT * FROM tag";
        return jdbcTemplate.query(QUERY_FIND_ALL_TAGS, new TagMapper());
    }

    @Override
    public boolean delete(Long id) {
        final String QUERY_DELETE_TAG_BY_ID = "DELETE FROM tag WHERE id = ?;";
        try {
            unlinkTables(id);
            return jdbcTemplate.update(QUERY_DELETE_TAG_BY_ID, id) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public Optional<Tag> update(Long id, Tag tag) {
        return Optional.empty();
    }

    @Override
    public List<Tag> saveTagList(List<Tag> tagList, long giftCertificateId) {

        final String QUERY_SAVE_TAG_LIST =
                "WITH result AS ( SELECT * FROM tag WHERE name = ? ), " +
                        "item AS (" +
                        "INSERT INTO tag(name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM result) " +
                        "RETURNING id, name) SELECT id, name FROM result UNION ALL SELECT id, name FROM item;";

        tagList = tagList.stream().map(tag -> jdbcTemplate
                        .queryForObject(QUERY_SAVE_TAG_LIST, new TagMapper(), tag.getName(), tag.getName()))
                .collect(Collectors.toList());
        // CONNECT TWO TABLES
        linkTables(tagList, giftCertificateId);

        return tagList;
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

    @Override
    public List<Tag> getByGiftId(long id) {
        final String QUERY_FIND_TAG_BY_GIFT_ID = "SELECT * FROM tag t INNER JOIN gift_certificate_tag gct " +
                "ON gct.tag_id = t.id WHERE gct.gift_id = ?;";
        return jdbcTemplate.query(QUERY_FIND_TAG_BY_GIFT_ID, new TagMapper(), id);
    }

    private void linkTables(List<Tag> tagList, Long giftCertificateId) {
        final String QUERY_LINK_TABLES = "INSERT INTO gift_certificate_tag(gift_id, tag_id) " +
                "VALUES(?, ?);";
        for (Tag tag : tagList) {
            jdbcTemplate.update(QUERY_LINK_TABLES, giftCertificateId, tag.getId());
        }
    }

    private void unlinkTables(Long tagId){
        final String QUERY_UNLINK_TABLES = "DELETE FROM gift_certificate_tag WHERE tag_id = ?;";
        jdbcTemplate.update(QUERY_UNLINK_TABLES, tagId);
    }
}
