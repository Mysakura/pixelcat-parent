package com.pixelcat.pixelcat.web.dao;

import com.pixelcat.pixelcat.web.domain.NameSpaceConfig;

import java.util.List;

public interface NameSpaceConfigDAO {

    int addNameSpace(NameSpaceConfig nameSpace);

    int updateNameSpace(NameSpaceConfig nameSpace);

    int deleteNameSpace(List<Long> ids);

    List<NameSpaceConfig> getNameSpaceList(NameSpaceConfig nameSpace);

    int batchAdd(List<NameSpaceConfig> list);

}
