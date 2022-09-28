package com.epam.esm.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class AppConfiguration {

//    @Bean
//    public ModelMapper modelMapper(){
//        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        return new ModelMapper();
//    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(
            "jdbc:postgresql://localhost:5432/gift_certificate_system",
                "postgres",
                "8"
        );
        driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
        return driverManagerDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

}
