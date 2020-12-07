package com.pixelcat.pixelcat.web.dao.impl;

import com.pixelcat.core.db.executor.Executor;
import com.pixelcat.core.db.executor.ExecutorFactory;
import com.pixelcat.pixelcat.web.dao.NameSpaceConfigDAO;
import com.pixelcat.pixelcat.web.domain.NameSpaceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NameSpaceConfigDAOImpl implements NameSpaceConfigDAO {

    @Autowired
    private ExecutorFactory executorFactory;

    @Override
    public int addNameSpace(NameSpaceConfig nameSpace) {
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
    public int updateNameSpace(NameSpaceConfig nameSpace) {
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
    public int deleteNameSpace(List<Long> ids) {
        Executor executor = executorFactory.newExecutor();
        try {
            int i = executor.delete(NameSpaceConfig.class, ids);
            executor.commit();
            return i;
        }finally {
            executor.close();
        }
    }

    @Override
    public List<NameSpaceConfig> getNameSpaceList(NameSpaceConfig nameSpace) {
        Executor executor = executorFactory.newExecutor();
        try {
            List<NameSpaceConfig> list = executor.getList(NameSpaceConfig.class, nameSpace);
            executor.commit();
            return list;
        }finally {
            executor.close();
        }
    }

    @Override
    public int batchAdd(List<NameSpaceConfig> list) {
        Executor executor = executorFactory.newExecutor();
        try {
            int i = executor.batchAdd(list);
            executor.commit();
            return i;
        }finally {
            executor.close();
        }
    }
}
