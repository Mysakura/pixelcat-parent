package com.pixelcat.pixelcat.web.service.impl;

import com.pixelcat.pixelcat.web.base.BasePageResponse;
import com.pixelcat.pixelcat.web.base.BaseResponse;
import com.pixelcat.pixelcat.web.base.dto.NameSpaceConfigDTO;
import com.pixelcat.pixelcat.web.base.enums.DeleteEnum;
import com.pixelcat.pixelcat.web.base.request.NameSpaceConfigRequest;
import com.pixelcat.pixelcat.web.dao.NameSpaceConfigDAO;
import com.pixelcat.pixelcat.web.domain.NameSpace;
import com.pixelcat.pixelcat.web.domain.NameSpaceConfig;
import com.pixelcat.pixelcat.web.service.NameSpaceConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NameSpaceConfigServiceImpl implements NameSpaceConfigService {

    @Autowired
    private NameSpaceConfigDAO nameSpaceConfigDAO;

    @Override
    public BasePageResponse<NameSpaceConfigDTO> getNameSpaceConfigList(NameSpaceConfigRequest request) {
        BasePageResponse<NameSpaceConfigDTO> response = new BasePageResponse<>();
        NameSpaceConfig record = new NameSpaceConfig();
        record.setDeleteFlag(DeleteEnum.NO.getCode());
        record.setNamespaceId(request.getNamespaceId());
        List<NameSpaceConfig> nameSpaceList = nameSpaceConfigDAO.getNameSpaceConfigList(record);

        List<NameSpaceConfigDTO> dataList = new ArrayList<>();
        nameSpaceList.forEach(e -> {
            NameSpaceConfigDTO dto = new NameSpaceConfigDTO();
            BeanUtils.copyProperties(e, dto);
            dataList.add(dto);
        });
        response.setDataList(dataList);
        return response;
    }

    @Override
    public BaseResponse<Integer> addNameSpaceConfig(NameSpaceConfigRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        NameSpaceConfig record = new NameSpaceConfig();
        BeanUtils.copyProperties(request, record);
        int i = nameSpaceConfigDAO.addNameSpaceConfig(record);
        response.setData(i);
        return response;
    }

    @Override
    public BaseResponse<Integer> batchAddNameSpaceConfig(NameSpaceConfigRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        String textModeContent = request.getTextModeContent();
        String[] kvs = textModeContent.split("\n");
        List<NameSpaceConfig> list = new ArrayList<>();
        for (String kv : kvs){
            String[] split = kv.split("=");
            String key = split[0].trim();
            String value = split[1].trim();
            NameSpaceConfig record = new NameSpaceConfig();
            record.setNamespaceId(request.getNamespaceId());
            record.setUsername(request.getUsername());
            record.setUpdateTime(request.getUpdateTime());
            record.setKey(key);
            record.setValue(value);
            list.add(record);
        }

        int i = nameSpaceConfigDAO.batchAddNameSpaceConfig(list);
        response.setData(i);
        return response;
    }

    @Override
    public BaseResponse<Integer> updateNameSpaceConfig(NameSpaceConfigRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        NameSpaceConfig record = new NameSpaceConfig();
        BeanUtils.copyProperties(request, record);
        int i = nameSpaceConfigDAO.updateNameSpaceConfig(record);
        response.setData(i);
        return response;
    }

    @Override
    public BaseResponse<Integer> deleteNameSpaceConfig(NameSpaceConfigRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        List<NameSpaceConfig> list = new ArrayList<>();
        request.getIds().forEach(id -> {
            NameSpaceConfig record = new NameSpaceConfig();
            BeanUtils.copyProperties(request, record);
            record.setId(id);
            record.setDeleteFlag(DeleteEnum.YES.getCode());
            list.add(record);
        });
        int i = nameSpaceConfigDAO.batchUpdateNameSpaceConfig(list);
        response.setData(i);
        return response;
    }
}
