package com.epam.esm.repository;

import com.epam.esm.configuration.TestConfiguration;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.tag.TagRepoImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void canCreateTag() {
        createTagTestTable();
        Tag expected = new Tag(1L, "testTag");
        Tag actual = tagRepo.create(expected);
        assertEquals(expected, actual);
    }


    private void createTagTestTable() {
        final String QUERY_CREATE_TEST_TABLE =
                "CREATE TABLE tag(id BIGSERIAL PRIMARY KEY, name VARCHAR NOT NULL);";
        jdbcTemplate.update(QUERY_CREATE_TEST_TABLE);
    }
}
