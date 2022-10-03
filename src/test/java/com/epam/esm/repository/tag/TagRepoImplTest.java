package com.epam.esm.repository.tag;

import com.epam.esm.configuration.TestConfiguration;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.tag.TagRepoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {TestConfiguration.class},
        loader = AnnotationConfigContextLoader.class
)
public class TagRepoImplTest {
    @Autowired
    private TagRepoImpl tagRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Tag tag;
    @BeforeEach
    void setUp(){
        tag = new Tag(1L, "tagForTest");
    }

    @Test
    void canCreateTag() {
        createTagTestTable();
        Tag actual = tagRepo.create(tag);
        assertEquals(tag.getName(), actual.getName());
    }

    @Test
    void canGetTagById() {
        Optional<Tag> tag1 = tagRepo.get(10000L);
        Optional<Tag> tag2 = tagRepo.get(tag.getId());
        assertTrue(tag1.isEmpty());
        assertTrue(tag2.isPresent());
        assertEquals("tagForTest", tag2.get().getName());
    }

    @Test
    void canGetAllTags() {
        List<Tag> list = tagRepo.getList();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    @Test
    void canDeleteTagById() {
        Tag expected = tagRepo.create(tag);

        assertTrue(tagRepo.delete(expected.getId()));
        assertTrue(tagRepo.get(expected.getId()).isEmpty());
    }

    @Test
    void canFindTagByName() {
        Tag actual = tagRepo.create(tag);
        Optional<Tag> expected = tagRepo.findTagByName(actual.getName());

        assertTrue(expected.isPresent());
        assertEquals(expected.get().getName(), actual.getName());
    }

    private void createTagTestTable() {
        final String QUERY_CREATE_TEST_TABLE =
                "CREATE TABLE tag(id BIGSERIAL PRIMARY KEY, name VARCHAR NOT NULL);";
        jdbcTemplate.update(QUERY_CREATE_TEST_TABLE);
    }

//    private void createTestTables(){
//        final String QUERY_CREATE_GIFT_CERTIFICATE_TABLE =
//                "CREATE TABLE gift_certificate(" +
//                        "id BIGSERIAL PRIMARY KEY, " +
//                        "name VARCHAR," +
//                        "description VARCHAR," +
//                        "duration INTEGER," +
//                        "price NUMERIC," +
//                        "create_date TIMESTAMP," +
//                        "last_update_date TIMESTAMP);";
//
//        final String QUERY_CREATE_GIFT_TAG_TEST_TABLE =
//                "CREATE TABLE gift_certificate_tag" +
//                        "(tag_id BIGINT, " +
//                        "gift_id BIGINT)," +
//                        "CONSTRAINT fk_gct_gift_id FOREIGN KEY (gift_id) REFERENCES gift_certificate (id)," +
//                        "CONSTRAINT fk_gct_tag_id FOREIGN KEY (tag_id) REFERENCES tag_id(id);";
//        jdbcTemplate.update(QUERY_CREATE_GIFT_CERTIFICATE_TABLE);
//        jdbcTemplate.update(QUERY_CREATE_GIFT_TAG_TEST_TABLE);
//    }
}
