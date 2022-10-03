package com.epam.esm.repository.giftCertificate;

import com.epam.esm.configuration.TestConfiguration;
import com.epam.esm.model.entity.GiftCertificate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {TestConfiguration.class},
        loader = AnnotationConfigContextLoader.class
)
public class GiftCertificateRepoImplTest {
    @Autowired
    private GiftCertificateRepoImpl giftCertificateRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private GiftCertificate giftCertificate;

    @BeforeEach
    void setUp() {
        giftCertificate = new GiftCertificate(
                1L, "testGiftCertificate", "good", BigDecimal.valueOf(1000),
                120, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    void canCreateGiftCertificate(){
        createTestTables();
        GiftCertificate expected = giftCertificateRepo.create(giftCertificate);
        assertNotNull(expected);
        assertEquals("testGiftCertificate", expected.getName());
    }

    @Test
    void canGetGiftCertificateById(){
        GiftCertificate expected = giftCertificateRepo.create(giftCertificate);
        assertNotNull(expected);

        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepo.get(expected.getId());
        assertTrue(optionalGiftCertificate.isPresent());
        assertEquals(optionalGiftCertificate.get().getName(), "testGiftCertificate");
    }

    @Test
    void canGetList(){
        giftCertificateRepo.create(giftCertificate);
        List<GiftCertificate> certificateList = giftCertificateRepo.getList();
        assertNotNull(certificateList);
        assertTrue(giftCertificateRepo.getList().size() > 0);
    }

    @Test
    void canDeleteById(){
        GiftCertificate certificate = giftCertificateRepo.create(giftCertificate);
        assertTrue(giftCertificateRepo.delete(certificate.getId()));
    }

    @Test
    void canUpdate(){
        GiftCertificate certificate = giftCertificateRepo.create(giftCertificate);
        GiftCertificate newCertificate = new GiftCertificate(
                1000L, "update", "good", BigDecimal.valueOf(1000),
                120, LocalDateTime.now(), LocalDateTime.now()
        );

        Optional<GiftCertificate> update = giftCertificateRepo.update(certificate.getId(), newCertificate);
        assertTrue(update.isPresent());
        assertEquals(update.get().getName(), "update");
        assertEquals(update.get().getId(), certificate.getId());
    }


    private void createTestTables() {

        final String QUERY_CREATE_TEST_TABLES =
                "DROP TABLE IF EXISTS gift_certificate; " +
                        "DROP TABLE IF EXISTS tag; " +
                        "DROP TABLE IF EXISTS gift_certificate_tag; " +
                        "CREATE TABLE gift_certificate(" +
                        "id BIGSERIAL PRIMARY KEY, " +
                        "name VARCHAR," +
                        "description VARCHAR," +
                        "duration INTEGER," +
                        "price NUMERIC," +
                        "create_date TIMESTAMP," +
                        "last_update_date TIMESTAMP); " +
                        "CREATE TABLE tag(id BIGSERIAL PRIMARY KEY, name VARCHAR NOT NULL); " +
                        "CREATE TABLE gift_certificate_tag" +
                        "(tag_id BIGINT, " +
                        "gift_id BIGINT)," +
                        "CONSTRAINT fk_gct_gift_id FOREIGN KEY (gift_id) REFERENCES gift_certificate (id)," +
                        "CONSTRAINT fk_gct_tag_id FOREIGN KEY (tag_id) REFERENCES tag_id(id);";

        jdbcTemplate.update(QUERY_CREATE_TEST_TABLES);
    }
}
