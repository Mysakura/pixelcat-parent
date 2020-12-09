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
    public int addNameSpaceConfig(NameSpaceConfig nameSpace) {
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
    public int updateNameSpaceConfig(NameSpaceConfig nameSpace) {
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
    public int batchUpdateNameSpaceConfig(List<NameSpaceConfig> nameSpaces) {
        Executor executor = executorFactory.newExecutor();
        try {
            int i = executor.batchUpdateById(nameSpaces);
            executor.commit();
            return i;
        }finally {
            executor.close();
        }
    }

    @Override
    public int deleteNameSpaceConfig(List<Long> ids) {
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
    public List<NameSpaceConfig> getNameSpaceConfigList(NameSpaceConfig nameSpace) {
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
    public List<NameSpaceConfig> getNameSpaceConfigList(NameSpaceConfig nameSpace, int limitStart, int limitEnd) {
        Executor executor = executorFactory.newExecutor();
        try {
            List<NameSpaceConfig> list = executor.getList(NameSpaceConfig.class, nameSpace, limitStart, limitEnd);
            executor.commit();
            return list;
        }finally {
            executor.close();
        }
    }

    @Override
    public int batchAddNameSpaceConfig(List<NameSpaceConfig> list) {
        Executor executor = executorFactory.newExecutor();
        try {
            int i = executor.batchAdd(list);
            executor.commit();
            return i;
        }finally {
            executor.close();
        }
    }

    @Override
    public int countNameSpaceConfig(NameSpaceConfig nameSpace) {
        Executor executor = executorFactory.newExecutor();
        try {
            int count = executor.count(NameSpaceConfig.class, nameSpace);
            executor.commit();
            return count;
        }finally {
            executor.close();
        }
    }
}
