package com.pixelcat.pixelcat.web.domain;

import com.pixelcat.core.db.parse.DbColumn;
import com.pixelcat.core.db.parse.DbId;
import com.pixelcat.core.db.parse.DbTable;
import lombok.Data;

import java.util.Date;

@Data
@DbTable("namespace")
public class NameSpace {

    @DbId
    @DbColumn("id")
    private Long id;

    @DbColumn("name")
    private String name;

    @DbColumn("project_id")
    private Long projectId;

    @DbColumn("env_id")
    private Long envId;

    @DbColumn("type")
    private Integer type;

    @DbColumn("username")
    private String username;

    @DbColumn("update_time")
    private Date updateTime;

    @DbColumn("delete_flag")
    private Integer deleteFlag;

}
