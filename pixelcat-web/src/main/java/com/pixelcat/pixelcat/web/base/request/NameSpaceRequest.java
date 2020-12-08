package com.pixelcat.pixelcat.web.base.request;

import com.pixelcat.pixelcat.web.base.BaseRequest;
import lombok.Data;

import java.util.Date;

@Data
public class NameSpaceRequest extends BaseRequest {

    private Long id;

    private String name;

    private String projectName;

    private String envName;

    private Integer type;

    private Integer deleteFlag;

    private String username;

    private Date updateTime;
}
