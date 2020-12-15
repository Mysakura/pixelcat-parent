package com.pixelcat.spring.boot.autoconfigure.domain;

import lombok.Data;

import java.util.List;

@Data
public class NameSpaceInit {

    private Long id;

    private String name;

    private Long projectId;

    private Long envId;

    private Integer type;

    private List<ConfigDTO> configList;

    @Data
    public static class ConfigDTO{
        private String key;

        private String value;

        private Long namespaceId;
    }
}
