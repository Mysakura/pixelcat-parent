package com.pixelcat.pixelcat.web.service;

import com.pixelcat.pixelcat.web.base.BasePageResponse;
import com.pixelcat.pixelcat.web.base.BaseResponse;
import com.pixelcat.pixelcat.web.base.dto.NameSpaceConfigDTO;
import com.pixelcat.pixelcat.web.base.request.NameSpaceConfigRequest;

public interface NameSpaceConfigService {

    BasePageResponse<NameSpaceConfigDTO> getNameSpaceConfigList(NameSpaceConfigRequest request);

    BaseResponse<Integer> addNameSpaceConfig(NameSpaceConfigRequest request);

    BaseResponse<Integer> batchAddNameSpaceConfig(NameSpaceConfigRequest request);

    BaseResponse<Integer> updateNameSpaceConfig(NameSpaceConfigRequest request);

    BaseResponse<Integer> deleteNameSpaceConfig(NameSpaceConfigRequest request);
}
