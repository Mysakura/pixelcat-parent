package com.pixelcat.pixelcat.web.dao;

import com.pixelcat.pixelcat.web.domain.NameSpaceConfig;

import java.util.List;

public interface NameSpaceConfigDAO {

    int addNameSpaceConfig(NameSpaceConfig nameSpace);

    int updateNameSpaceConfig(NameSpaceConfig nameSpace);

    int batchUpdateNameSpaceConfig(List<NameSpaceConfig> nameSpaces);

    int deleteNameSpaceConfig(List<Long> ids);

    List<NameSpaceConfig> getNameSpaceConfigList(NameSpaceConfig nameSpace);

    List<NameSpaceConfig> getNameSpaceConfigList(NameSpaceConfig nameSpace, int limitStart, int limitEnd);

    int batchAddNameSpaceConfig(List<NameSpaceConfig> list);

    int countNameSpaceConfig(NameSpaceConfig nameSpace);

}
