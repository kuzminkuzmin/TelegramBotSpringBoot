package com.dmitriikuzmin.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "project-settings")
public class DataList {
    private List<Compliment> compliments;
}