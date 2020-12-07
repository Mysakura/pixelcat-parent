package com.pixelcat.spring.boot.autoconfigure.init;

import com.pixelcat.core.db.parse.DbColumn;
import com.pixelcat.core.db.parse.DbId;
import com.pixelcat.core.db.parse.DbTable;
import lombok.Data;

@Data
@DbTable("namespace")
public class NameSpace {

    @DbId
    @DbColumn("id")
    private long id;

    @DbColumn("name")
    private String name;

    @DbColumn("project_name")
    private String projectName;

    @DbColumn("env_name")
    private String envName;

    @DbColumn("type")
    private int type;

    @DbColumn("delete_flag")
    private int deleteFlag;

}
