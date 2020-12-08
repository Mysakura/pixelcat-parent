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
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private NameSpaceService nameSpaceService;

    @RequestMapping("/list")
    public BasePageResponse<NameSpaceDTO> getProjectList(){
        return nameSpaceService.getProjectList();
    }

    @RequestMapping("/add")
    public BaseResponse<Integer> addProject(@RequestBody NameSpaceRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceService.addProject(request);
    }

    @RequestMapping("/update")
    public BaseResponse<Integer> updateProject(@RequestBody NameSpaceRequest request){
        request.setUsername("Admin");
        request.setUpdateTime(new Date());
        return nameSpaceService.updateProject(request);
    }
}
