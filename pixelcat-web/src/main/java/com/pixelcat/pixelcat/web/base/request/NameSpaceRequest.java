package com.pixelcat.pixelcat.web.base.request;

import com.pixelcat.pixelcat.web.base.BaseRequest;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class NameSpaceRequest extends BaseRequest {

    private Long id;

    private List<Long> ids;

    private String name;

    private Long projectId;

    private Long envId;

    private Integer type;

    private Integer deleteFlag;

    private String username;

    private Date updateTime;
}
