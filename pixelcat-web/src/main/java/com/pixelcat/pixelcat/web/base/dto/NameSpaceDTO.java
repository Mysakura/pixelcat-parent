package com.pixelcat.pixelcat.web.base.dto;

import com.pixelcat.pixelcat.web.domain.NameSpace;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
public class NameSpaceDTO {

    private Long id;

    private String name;

    private Long projectId;

    private Long envId;

    private String projectName;

    private String envName;

    private List<NameSpace> envList;

    private Integer type;

    private String username;

    private Date updateTime;

    private String updateTimeStr;

    private Integer deleteFlag;

    public String getUpdateTimeStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateTime);
    }
}
