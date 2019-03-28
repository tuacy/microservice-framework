package com.tuacy.microservice.framework.user.manage.service.impl;

import com.tuacy.microservice.framework.user.manage.service.IStarterCustomService;
import com.tuacy.starter.url.decoration.UrlDecorationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "starter-custom")
public class StarterCustomServiceImpl implements IStarterCustomService {

    private UrlDecorationService urlDecorationService;

    @Autowired
    public void setUrlDecorationService(UrlDecorationService urlDecorationService) {
        this.urlDecorationService = urlDecorationService;
    }

    @Override
    public String urlDecoration(String param) {
        return urlDecorationService.url(param);
    }

}
