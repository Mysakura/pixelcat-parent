package com.pixelcat.spring.boot.autoconfigure.domain;

import com.pixelcat.core.db.parse.DbColumn;
import com.pixelcat.core.db.parse.DbId;
import com.pixelcat.core.db.parse.DbTable;
import lombok.Data;

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
    private int type;

    @DbColumn("delete_flag")
    private int deleteFlag;

}
