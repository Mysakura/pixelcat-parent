package com.pixelcat.pixelcat.web.service.impl;

import com.pixelcat.core.zk.handle.ConfigNodeHandler;
import com.pixelcat.pixelcat.web.base.BasePageResponse;
import com.pixelcat.pixelcat.web.base.BaseResponse;
import com.pixelcat.pixelcat.web.base.dto.NameSpaceConfigDTO;
import com.pixelcat.pixelcat.web.base.enums.DeleteEnum;
import com.pixelcat.pixelcat.web.base.enums.StatusEnum;
import com.pixelcat.pixelcat.web.base.request.NameSpaceConfigRequest;
import com.pixelcat.pixelcat.web.dao.NameSpaceConfigDAO;
import com.pixelcat.pixelcat.web.dao.NameSpaceDAO;
import com.pixelcat.pixelcat.web.domain.NameSpace;
import com.pixelcat.pixelcat.web.domain.NameSpaceConfig;
import com.pixelcat.pixelcat.web.service.NameSpaceConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class NameSpaceConfigServiceImpl implements NameSpaceConfigService {

    @Autowired
    private NameSpaceDAO nameSpaceDAO;

    @Autowired
    private NameSpaceConfigDAO nameSpaceConfigDAO;

    @Autowired
    private ConfigNodeHandler configNodeHandler;

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
        if (i > 0){
            syncZookeeper(request.getNamespaceId(), StatusEnum.ADD);
        }
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
        if (i > 0){
            syncZookeeper(request.getNamespaceId(), StatusEnum.ADD);
        }
        response.setData(i);
        return response;
    }

    @Override
    public BaseResponse<Integer> updateNameSpaceConfig(NameSpaceConfigRequest request) {
        BaseResponse<Integer> response = new BaseResponse<>();
        NameSpaceConfig record = new NameSpaceConfig();
        BeanUtils.copyProperties(request, record);
        int i = nameSpaceConfigDAO.updateNameSpaceConfig(record);
        if (i > 0){
            syncZookeeperById(request.getId(), StatusEnum.UPDATE);
        }
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
        if (i > 0){
            syncZookeeperById(request.getIds().get(0), StatusEnum.DELETE);
        }
        response.setData(i);
        return response;
    }

    /**
     * 新增，必须传入namespaceId
     * @param namespaceId
     */
    private void syncZookeeper(Long namespaceId, StatusEnum status){
        NameSpace record = new NameSpace();
        record.setId(namespaceId);
        List<NameSpace> list = nameSpaceDAO.getNameSpaceList(record);
        if (!CollectionUtils.isEmpty(list)){
            NameSpace nameSpace = list.get(0);
            String path = "/" + nameSpace.getProjectId() + "/" + nameSpace.getEnvId() + "/" + nameSpace.getName();
            if (configNodeHandler.isExist(path)) {
                configNodeHandler.setPathValue(path, status.getName());
            }else {
                configNodeHandler.createEphemeralPath(path, status.getName());
            }
        }
    }

    /**
     * 修改删除，必须传入id
     * @param id
     */
    private void syncZookeeperById(Long id, StatusEnum status){
        NameSpaceConfig record = new NameSpaceConfig();
        record.setId(id);
        List<NameSpaceConfig> nameSpaceList = nameSpaceConfigDAO.getNameSpaceConfigList(record);
        if (!CollectionUtils.isEmpty(nameSpaceList)){
            syncZookeeper(nameSpaceList.get(0).getNamespaceId(), status);
        }
    }
}
