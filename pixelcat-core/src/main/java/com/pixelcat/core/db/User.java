package com.pixelcat.core.db;

import com.pixelcat.core.db.parse.DbColumn;
import com.pixelcat.core.db.parse.DbId;
import com.pixelcat.core.db.parse.DbTable;
import lombok.Data;

@Data
@DbTable("user")
public class User {

    @DbId
    @DbColumn("id")
    private Integer id;
    @DbColumn("user_name")
    private String userName;
    @DbColumn("user_age")
    private Integer userAge;

    public User() {
    }

    public User(Integer id, String userName, Integer userAge) {
        this.id = id;
        this.userName = userName;
        this.userAge = userAge;
    }
}