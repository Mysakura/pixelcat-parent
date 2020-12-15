package com.pixelcat.pixelcat.web.controller;


import com.pixelcat.pixelcat.web.base.BasePageResponse;
import com.pixelcat.pixelcat.web.base.BaseResponse;
import com.pixelcat.pixelcat.web.base.dto.NameSpaceDTO;
import com.pixelcat.pixelcat.web.base.request.NameSpaceRequest;
import com.pixelcat.pixelcat.web.service.NameSpaceService;
import com.pixelcat.spring.boot.autoconfigure.domain.NameSpaceInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/namespace")
public class NameSpaceController {

    @Autowired
    private NameSpaceService nameSpaceService;

    @RequestMapping("/initConfig")
    public BasePageResponse<NameSpaceInit> initConfig(@RequestBody NameSpaceRequest request){
        return nameSpaceService.initConfig(request);
    }

    @RequestMapping("/list")
    public BasePageResponse<NameSpaceDTO> getNameSpaceList(@RequestBody NameSpaceRequest request){
        return nameSpaceService.getNameSpaceList(request);
    }

    @RequestMapping("/add")
    public BaseResponse<Integer> addNameSpace(@RequestBody NameSpaceRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceService.addNameSpace(request);
    }

    @RequestMapping("/update")
    public BaseResponse<Integer> updateNameSpace(@RequestBody NameSpaceRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceService.updateNameSpace(request);
    }

    @RequestMapping("/delete")
    public BaseResponse<Integer> deleteNameSpace(@RequestBody NameSpaceRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceService.deleteNameSpace(request);
    }
}
