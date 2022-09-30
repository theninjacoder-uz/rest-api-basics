package com.epam.esm.repository.giftCertificate;

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
    public GiftCertificate create(GiftCertificate giftCertificate) {
        final String GIFT_CREATION_QUERY =
                "WITH result AS (INSERT INTO gift_certificate(name, description, price, duration, create_date, last_update_date) " +
                        "VALUES(?, ?, ?, ?, ?, ?) RETURNING id, name, description, price, duration, create_date, last_update_date)\n " +
                        "SELECT * FROM result;";
        return  jdbcTemplate.queryForObject(GIFT_CREATION_QUERY,
                new GiftCertificateMapper(),
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                giftCertificate.getCreateDate(),
                giftCertificate.getLastUpdatedDate());
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
        final String QUERY_DELETE_GIFT_BY_ID = "DELETE FROM gift_certificate WHERE id = ?;";
        try {
            unlinkTables(id);
            return jdbcTemplate.update(QUERY_DELETE_GIFT_BY_ID, id) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> update(Long id, GiftCertificate giftCertificate) {
        final String QUERY_UPDATE = "WITH result AS(UPDATE gift_certificate SET" +
                " name = ?, description = ?, price = ?, duration = ?, last_update_date = ? WHERE id = ? " +
                "RETURNING id, name, description, price, duration, create_date, last_update_date) " +
                "SELECT * FROM result;";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    QUERY_UPDATE,
                    new GiftCertificateMapper(),
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

        if (sortByDate != null || sortByName != null) {
            QUERY_SEARCH = QUERY_SEARCH + " order by " + ((sortByDate != null) ? ("create_date " + sortByDate + " ") : " ")
                    + ((sortByName != null) ? ("name " + sortByName + " ") : "");
        }
        QUERY_SEARCH += ";";

        return jdbcTemplate.query(QUERY_SEARCH, new GiftCertificateMapper(), word, tagName);
    }

    private void unlinkTables(Long giftCertificateId){
        final String QUERY_UNLINK_TABLES = "DELETE FROM gift_certificate_tag WHERE gift_id = ?;";
        jdbcTemplate.update(QUERY_UNLINK_TABLES, giftCertificateId);
    }
}
