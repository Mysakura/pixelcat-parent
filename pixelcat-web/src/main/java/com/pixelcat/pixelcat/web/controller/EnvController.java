package com.pixelcat.pixelcat.web.controller;


import com.pixelcat.pixelcat.web.base.BasePageResponse;
import com.pixelcat.pixelcat.web.base.BaseResponse;
import com.pixelcat.pixelcat.web.base.dto.NameSpaceDTO;
import com.pixelcat.pixelcat.web.base.request.NameSpaceRequest;
import com.pixelcat.pixelcat.web.service.NameSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/env")
public class EnvController {

    @Autowired
    private NameSpaceService nameSpaceService;

    @RequestMapping("/list")
    public BasePageResponse<NameSpaceDTO> getEnvList(@RequestBody NameSpaceRequest request){
        return nameSpaceService.getEnvList(request);
    }

    @RequestMapping("/add")
    public BaseResponse<Integer> addEnv(@RequestBody NameSpaceRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceService.addEnv(request);
    }

    @RequestMapping("/update")
    public BaseResponse<Integer> updateEnv(@RequestBody NameSpaceRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceService.updateEnv(request);
    }

    @RequestMapping("/delete")
    public BaseResponse<Integer> deleteEnv(@RequestBody NameSpaceRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceService.deleteEnv(request);
    }
}
