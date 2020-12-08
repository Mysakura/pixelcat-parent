package com.pixelcat.pixelcat.web.service;

import com.pixelcat.pixelcat.web.base.BasePageResponse;
import com.pixelcat.pixelcat.web.base.BaseResponse;
import com.pixelcat.pixelcat.web.base.dto.NameSpaceDTO;
import com.pixelcat.pixelcat.web.base.request.NameSpaceRequest;

public interface NameSpaceService {

    BasePageResponse<NameSpaceDTO> getProjectList();

    BaseResponse<Integer> addProject(NameSpaceRequest request);

    BaseResponse<Integer> updateProject(NameSpaceRequest request);

    BaseResponse<Integer> deleteProject(NameSpaceRequest request);

    BasePageResponse<NameSpaceDTO> getEnvList(NameSpaceRequest request);

    BaseResponse<Integer> addEnv(NameSpaceRequest request);

    BaseResponse<Integer> updateEnv(NameSpaceRequest request);

    BaseResponse<Integer> deleteEnv(NameSpaceRequest request);

}
