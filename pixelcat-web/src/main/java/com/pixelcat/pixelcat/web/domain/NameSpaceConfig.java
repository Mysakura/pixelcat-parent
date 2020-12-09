package com.pixelcat.pixelcat.web.domain;

import com.pixelcat.core.db.parse.DbColumn;
import com.pixelcat.core.db.parse.DbId;
import com.pixelcat.core.db.parse.DbTable;
import lombok.Data;

import java.util.Date;

@Data
@DbTable("namespace_config")
public class NameSpaceConfig {

    @DbId
    @DbColumn("id")
    private Long id;

    @DbColumn("config_key")
    private String key;

    @DbColumn("config_value")
    private String value;

    @DbColumn("namespace_id")
    private Long namespaceId;

    @DbColumn("username")
    private String username;

    @DbColumn("update_time")
    private Date updateTime;

    @DbColumn("delete_flag")
    private Integer deleteFlag;

}
