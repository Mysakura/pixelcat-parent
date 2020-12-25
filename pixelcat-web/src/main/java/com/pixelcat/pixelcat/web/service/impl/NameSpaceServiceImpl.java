package com.pixelcat.pixelcat.web.service.impl;

import com.pixelcat.pixelcat.web.base.BasePageResponse;
import com.pixelcat.pixelcat.web.base.BaseResponse;
import com.pixelcat.pixelcat.web.base.dto.NameSpaceDTO;
import com.pixelcat.pixelcat.web.base.enums.DeleteEnum;
import com.pixelcat.pixelcat.web.base.enums.NameSpaceEnum;
import com.pixelcat.pixelcat.web.base.request.NameSpaceRequest;
import com.pixelcat.pixelcat.web.dao.NameSpaceConfigDAO;
import com.pixelcat.pixelcat.web.dao.NameSpaceDAO;
import com.pixelcat.pixelcat.web.domain.NameSpace;
import com.pixelcat.pixelcat.web.domain.NameSpaceConfig;
import com.pixelcat.pixelcat.web.service.NameSpaceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NameSpaceServiceImpl implements NameSpaceService {

    @Autowired
    private NameSpaceDAO nameSpaceDAO;

    @Autowired
    private NameSpaceConfigDAO nameSpaceConfigDAO;

    @Override
    public BasePageResponse<NameSpaceDTO> getProjectList() {
        BasePageResponse<NameSpaceDTO> response = new BasePageResponse<>();
        NameSpace record = new NameSpace();
        record.setDeleteFlag(DeleteEnum.NO.getCode());
        record.setType(NameSpaceEnum.PROJECT.getCode());
        List<NameSpace> projectList = nameSpaceDAO.getNameSpaceList(record);

        record.setType(NameSpaceEnum.ENV.getCode());
        List<NameSpace> envList = nameSpaceDAO.getNameSpaceList(record);
        Map<Long, List<NameSpace>> collect = envList.stream().collect(Collectors.groupingBy(NameSpace::getProjectId));


        List<NameSpaceDTO> dataList = new ArrayList<>();
        projectList.forEach(e -> {
            NameSpaceDTO dto = new NameSpaceDTO();
            BeanUtils.copyProperties(e, dto);
            dto.setEnvList(collect.get(dto.getId()));
            dataList.add(dto);
        });
        response.setDataList(dataList);
        return response;
    }

    @Override
    public BaseResponse<Integer> addProject(NameSpaceRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        NameSpace record = new NameSpace();
        BeanUtils.copyProperties(request, record);
        int i = nameSpaceDAO.addNameSpace(record);
        response.setData(i);
        return response;
    }

    @Override
    public BaseResponse<Integer> updateProject(NameSpaceRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        NameSpace record = new NameSpace();
        BeanUtils.copyProperties(request, record);
        int i = nameSpaceDAO.updateNameSpace(record);
        response.setData(i);
        return response;
    }

    @Override
    public BaseResponse<Integer> deleteProject(NameSpaceRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        List<NameSpace> list = new ArrayList<>();
        request.getIds().forEach(id -> {
            NameSpace record = new NameSpace();
            BeanUtils.copyProperties(request, record);
            record.setId(id);
            record.setDeleteFlag(DeleteEnum.YES.getCode());
            list.add(record);
        });
        int i = nameSpaceDAO.batchUpdateNameSpace(list);
        response.setData(i);
        return response;
    }

    @Override
    public BasePageResponse<NameSpaceDTO> getEnvList(NameSpaceRequest request) {
        BasePageResponse<NameSpaceDTO> response = new BasePageResponse<>();
        NameSpace record = new NameSpace();
        record.setDeleteFlag(DeleteEnum.NO.getCode());
        record.setType(NameSpaceEnum.ENV.getCode());
        record.setProjectId(request.getProjectId());
        List<NameSpace> envList = nameSpaceDAO.getNameSpaceList(record);

        List<NameSpaceDTO> dataList = new ArrayList<>();
        envList.forEach(e -> {
            NameSpaceDTO dto = new NameSpaceDTO();
            BeanUtils.copyProperties(e, dto);
            dataList.add(dto);
        });
        response.setDataList(dataList);
        return response;
    }

    @Override
    public BaseResponse<Integer> addEnv(NameSpaceRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        NameSpace record = new NameSpace();
        BeanUtils.copyProperties(request, record);
        int i = nameSpaceDAO.addNameSpace(record);
        response.setData(i);
        return response;
    }

    @Override
    public BaseResponse<Integer> updateEnv(NameSpaceRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        NameSpace record = new NameSpace();
        BeanUtils.copyProperties(request, record);
        int i = nameSpaceDAO.updateNameSpace(record);
        response.setData(i);
        return response;
    }

    @Override
    public BaseResponse<Integer> deleteEnv(NameSpaceRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        List<NameSpace> list = new ArrayList<>();
        request.getIds().forEach(id -> {
            NameSpace record = new NameSpace();
            BeanUtils.copyProperties(request, record);
            record.setId(id);
            record.setDeleteFlag(DeleteEnum.YES.getCode());
            list.add(record);
        });
        int i = nameSpaceDAO.batchUpdateNameSpace(list);
        response.setData(i);
        return response;
    }

    @Override
    public BasePageResponse<NameSpaceDTO> getNameSpaceList(NameSpaceRequest request) {
        BasePageResponse<NameSpaceDTO> response = new BasePageResponse<>();
        NameSpace record = new NameSpace();
        record.setDeleteFlag(DeleteEnum.NO.getCode());
        record.setType(NameSpaceEnum.NAME_SPACE.getCode());
        record.setProjectId(request.getProjectId());
        record.setEnvId(request.getEnvId());
        List<NameSpace> nameSpaceList = nameSpaceDAO.getNameSpaceList(record, request.getLimitStart(), request.getLimitEnd());
        int count = nameSpaceDAO.countNameSpace(record);

        List<NameSpaceDTO> dataList = new ArrayList<>();
        nameSpaceList.forEach(e -> {
            NameSpaceDTO dto = new NameSpaceDTO();
            BeanUtils.copyProperties(e, dto);
            dataList.add(dto);
        });
        response.setDataList(dataList);
        response.setPage(request.getPage());
        response.setPageSize(request.getPageSize());
        response.setRecordCount(count);
        return response;
    }

    @Override
    public BaseResponse<Integer> addNameSpace(NameSpaceRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        NameSpace record = new NameSpace();
        BeanUtils.copyProperties(request, record);
        int i = nameSpaceDAO.addNameSpace(record);
        response.setData(i);
        return response;
    }

    @Override
    public BaseResponse<Integer> updateNameSpace(NameSpaceRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        NameSpace record = new NameSpace();
        BeanUtils.copyProperties(request, record);
        int i = nameSpaceDAO.updateNameSpace(record);
        response.setData(i);
        return response;
    }

    @Override
    public BaseResponse<Integer> deleteNameSpace(NameSpaceRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        List<NameSpace> list = new ArrayList<>();
        request.getIds().forEach(id -> {
            NameSpace record = new NameSpace();
            BeanUtils.copyProperties(request, record);
            record.setId(id);
            record.setDeleteFlag(DeleteEnum.YES.getCode());
            list.add(record);
        });
        int i = nameSpaceDAO.batchUpdateNameSpace(list);
        response.setData(i);
        return response;
    }

    @Override
    public BaseResponse<Map<String, Object>> singleConfig(NameSpaceRequest request) {
        Map<String, Object> result = new HashMap<>();
        BaseResponse<Map<String, Object>> response = new BaseResponse<>();

        NameSpace record = new NameSpace();
        record.setDeleteFlag(DeleteEnum.NO.getCode());
        record.setType(NameSpaceEnum.NAME_SPACE.getCode());
        record.setProjectId(request.getProjectId());
        record.setEnvId(request.getEnvId());
        record.setName(request.getName());
        List<NameSpace> nameSpaceList = nameSpaceDAO.getNameSpaceList(record);
        if (!CollectionUtils.isEmpty(nameSpaceList)){
            NameSpaceConfig config = new NameSpaceConfig();
            config.setDeleteFlag(DeleteEnum.NO.getCode());
            config.setNamespaceId(nameSpaceList.get(0).getId());
            List<NameSpaceConfig> configList = nameSpaceConfigDAO.getNameSpaceConfigList(config);
            if (!CollectionUtils.isEmpty(configList)){
                configList.forEach(c -> {
                    result.put(c.getKey(), c.getValue());
                });
                response.setData(result);
            }
        }

        return response;
    }
}
