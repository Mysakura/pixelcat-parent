package com.pixelcat.pixelcat.web.controller;

import com.pixelcat.pixelcat.web.base.BasePageResponse;
import com.pixelcat.pixelcat.web.base.BaseResponse;
import com.pixelcat.pixelcat.web.base.dto.NameSpaceConfigDTO;
import com.pixelcat.pixelcat.web.base.request.NameSpaceConfigRequest;
import com.pixelcat.pixelcat.web.service.NameSpaceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private NameSpaceConfigService nameSpaceConfigService;

    @RequestMapping("/list")
    public BasePageResponse<NameSpaceConfigDTO> getNameSpaceList(@RequestBody NameSpaceConfigRequest request){
        return nameSpaceConfigService.getNameSpaceConfigList(request);
    }

    @RequestMapping("/add")
    public BaseResponse<Integer> addNameSpace(@RequestBody NameSpaceConfigRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceConfigService.addNameSpaceConfig(request);
    }

    @RequestMapping("/batchAdd")
    public BaseResponse<Integer> batchAddNameSpace(@RequestBody NameSpaceConfigRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceConfigService.batchAddNameSpaceConfig(request);
    }

    @RequestMapping("/update")
    public BaseResponse<Integer> updateNameSpace(@RequestBody NameSpaceConfigRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceConfigService.updateNameSpaceConfig(request);
    }

    @RequestMapping("/delete")
    public BaseResponse<Integer> deleteNameSpace(@RequestBody NameSpaceConfigRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceConfigService.deleteNameSpaceConfig(request);
    }
}
