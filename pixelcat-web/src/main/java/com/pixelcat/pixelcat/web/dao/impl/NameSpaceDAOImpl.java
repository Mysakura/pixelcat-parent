package com.pixelcat.pixelcat.web.dao.impl;

import com.pixelcat.core.db.executor.Executor;
import com.pixelcat.core.db.executor.ExecutorFactory;
import com.pixelcat.pixelcat.web.dao.NameSpaceDAO;
import com.pixelcat.spring.boot.autoconfigure.init.NameSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NameSpaceDAOImpl implements NameSpaceDAO {

    @Autowired
    private ExecutorFactory executorFactory;

    @Override
    public int addNameSpace(NameSpace nameSpace) {
        Executor executor = executorFactory.newExecutor();
        try {
            int i = executor.add(nameSpace);
            executor.commit();
            return i;
        }finally {
            executor.close();
        }
    }

    @Override
    public int updateNameSpace(NameSpace nameSpace) {
        Executor executor = executorFactory.newExecutor();
        try {
            int i = executor.updateById(nameSpace);
            executor.commit();
            return i;
        }finally {
            executor.close();
        }
    }

    @Override
    public List<NameSpace> getNameSpaceList(NameSpace nameSpace) {
        Executor executor = executorFactory.newExecutor();
        try {
            List<NameSpace> list = executor.getList(NameSpace.class, nameSpace);
            executor.commit();
            return list;
        }finally {
            executor.close();
        }
    }
}
