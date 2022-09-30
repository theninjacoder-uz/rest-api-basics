package com.epam.esm.configuration;

import com.epam.esm.repository.giftCertificate.GiftCertificateRepoImpl;
import com.epam.esm.repository.tag.TagRepoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class TestConfiguration {

    @Bean
    DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testDb;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("ninja");
        dataSource.setPassword("ninja");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public TagRepoImpl tagRepo(JdbcTemplate jdbcTemplate){
        return new TagRepoImpl(jdbcTemplate);
    }

    @Bean
    public GiftCertificateRepoImpl giftCertificateRepo(JdbcTemplate jdbcTemplate){
        return new GiftCertificateRepoImpl(jdbcTemplate);
    }
}
