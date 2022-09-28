package com.epam.esm.repository.giftCertificate;

import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.mapper.giftCertificate.GiftCertificateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GiftCertificateRepoImpl implements GiftCertificateRepo {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<GiftCertificate> create(GiftCertificate giftCertificate) {
        final String GIFT_CREATION_QUERY =
                "WITH result AS (INSERT INTO gift_certificate(name, description, price, duration, create_date, last_update_date) " +
                        "VALUES(?, ?, ?, ?, ?, ?) RETURNING id, name, description, price, duration, create_date, last_update_date)\n " +
                        "SELECT * FROM result;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(GIFT_CREATION_QUERY,
                    GiftCertificate.class,
                    giftCertificate.getName(),
                    giftCertificate.getDescription(),
                    giftCertificate.getPrice(),
                    giftCertificate.getDuration(),
                    giftCertificate.getCreateDate(),
                    giftCertificate.getLastUpdatedDate()));
        } catch (DataAccessException ignored){
            return Optional.empty();
        }
    }

    @Override
    public Optional<GiftCertificate> get(Long id) {
        final String QUERY_GET_BY_ID = "SELECT * FROM gift_certificate WHERE id = ?;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(QUERY_GET_BY_ID,
                    new GiftCertificateMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GiftCertificate> getList() {
        final String QUERY_GET_LIST = "SELECT * FROM gift_certificate;";
        return jdbcTemplate.query(QUERY_GET_LIST, new GiftCertificateMapper());
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        final String QUERY_DELETE_RELATIONS = "DELETE FROM gift_certificate_tag WHERE gift_id = ?;";
        final String QUERY_DELETE_GIFT_BY_ID = "DELETE FROM gift_certificate WHERE id = ?;";
        try {
            jdbcTemplate.update(QUERY_DELETE_RELATIONS, id);
            return jdbcTemplate.update(QUERY_DELETE_GIFT_BY_ID, id) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public Optional<GiftCertificate> update(Long id, GiftCertificate giftCertificate) {
        final String QUERY_UPDATE = "WITH result AS(UPDATE gift_certificate SET" +
                " name = ?, description = ?, price = ?, duration = ?, last_update_date = ? WHERE id = ? " +
                "RETURNING id, name, description, price, duration, create_date, last_update_date)\n " +
                "SELECT * FROM result;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    QUERY_UPDATE,
                    GiftCertificate.class,
                    giftCertificate.getName(),
                    giftCertificate.getDescription(),
                    giftCertificate.getPrice(),
                    giftCertificate.getDuration(),
                    giftCertificate.getLastUpdatedDate(),
                    id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<GiftCertificate> findByParametersAndSort(String word, String tagName, String sortByDate, String sortByName) {
        String QUERY_SEARCH = "SELECT * FROM find_by_parameters(?, ?) ";

        if(sortByDate != null || sortByName != null){
            QUERY_SEARCH = QUERY_SEARCH + " order by " + ((sortByDate != null) ? (sortByDate + " ") : " ")
                    + ((sortByName != null) ? sortByName : "") + ";";
        }

        jdbcTemplate.query(QUERY_SEARCH, new GiftCertificateMapper(), word, tagName);

        return null;
    }

    private void linkTables(List<TagResponseDto> tags, Long giftCertificateId) {
        final String QUERY_LINK_TABLES = "INSERT INTO gift_certificate_tag(gift_id, tag_id) VALUES(?, ?);";
        tags.forEach(tag ->
                jdbcTemplate.update(QUERY_LINK_TABLES, giftCertificateId, tag.getId()));
    }
}
