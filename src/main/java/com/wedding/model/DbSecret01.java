package com.wedding.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DbSecret01 {

    private String username;
    private String password;
    private String host;
    private String port;
    private String dbname;
}
