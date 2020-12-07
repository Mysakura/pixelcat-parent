package com.pixelcat.pixelcat.web.dao;


import com.pixelcat.spring.boot.autoconfigure.init.NameSpace;

import java.util.List;

public interface NameSpaceDAO {

    int addNameSpace(NameSpace nameSpace);

    int updateNameSpace(NameSpace nameSpace);

    List<NameSpace> getNameSpaceList(NameSpace nameSpace);

}
