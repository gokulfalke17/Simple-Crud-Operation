package com.wedding.config;

import com.wedding.model.DbSecret01;
import com.wedding.service.SecretsService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final SecretsService secretsService;

    @Bean
    public DataSource dataSource() throws Exception {

        DbSecret01 db = secretsService.getDbSecret();

        if (db == null ||
                db.getHost() == null ||
                db.getPort() == null ||
                db.getDbname() == null ||
                db.getUsername() == null ||
                db.getPassword() == null) {

            throw new RuntimeException("❌ DbSecret-01 is missing required fields");
        }

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(
                "jdbc:postgresql://" + db.getHost() + ":" + db.getPort() + "/" + db.getDbname()
        );
        ds.setUsername(db.getUsername());
        ds.setPassword(db.getPassword());

        return ds;
    }
}
