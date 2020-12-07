package com.pixelcat.pixelcat.web.domain;

import com.pixelcat.core.db.parse.DbColumn;
import com.pixelcat.core.db.parse.DbId;
import com.pixelcat.core.db.parse.DbTable;
import lombok.Data;

@Data
@DbTable("namespace_config")
public class NameSpaceConfig {

    @DbId
    @DbColumn("id")
    private long id;

    @DbColumn("key")
    private String key;

    @DbColumn("value")
    private String value;

    @DbColumn("namespace_id")
    private long namespaceId;

    @DbColumn("delete_flag")
    private int deleteFlag;

}
