package com.pixelcat.pixelcat.web.dao;



import com.pixelcat.pixelcat.web.domain.NameSpace;

import java.util.List;

public interface NameSpaceDAO {

    int addNameSpace(NameSpace nameSpace);

    int updateNameSpace(NameSpace nameSpace);

    int batchUpdateNameSpace(List<NameSpace> nameSpaces);

    List<NameSpace> getNameSpaceList(NameSpace nameSpace);

    List<NameSpace> getNameSpaceList(NameSpace nameSpace, int limitStart, int limitEnd);

}
