package com.tuacy.microservice.framework.user.manage.controller;

import com.tuacy.microservice.framework.common.constant.ResponseResultType;
import com.tuacy.microservice.framework.common.controller.BaseController;
import com.tuacy.microservice.framework.common.entity.response.ResponseDataEntity;
import com.tuacy.microservice.framework.user.manage.api.api.IStarterCustomControllerApi;
import com.tuacy.microservice.framework.user.manage.api.request.UrlDecorationParam;
import com.tuacy.microservice.framework.user.manage.service.IStarterCustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StarterCustomController extends BaseController implements IStarterCustomControllerApi {

    private IStarterCustomService starterCustomService;

    @Autowired
    public void setStarterCustomService(IStarterCustomService starterCustomService) {
        this.starterCustomService = starterCustomService;
    }

    @Override
    public ResponseDataEntity<String> urlDecoration(@RequestBody UrlDecorationParam param) {
        ResponseDataEntity<String> responseDataEntity = new ResponseDataEntity<>();
        try {
            responseDataEntity.setMsg(ResponseResultType.SUCCESS.getDesc());
            responseDataEntity.setStatus(ResponseResultType.SUCCESS.getValue());
            responseDataEntity.setData(starterCustomService.urlDecoration(param.getParam()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseDataEntity;
    }

}
