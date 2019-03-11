package com.tuacy.microservice.framework.quartz.manage.api;

import com.tuacy.microservice.framework.common.entity.response.ResponseDataEntity;
import com.tuacy.microservice.framework.quartz.manage.param.QuartzJobAddParam;
import com.tuacy.microservice.framework.quartz.manage.param.QuartzJobDeleteParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/quartz/")
public interface IQuartzManageApi {


    /**
     * 添加一个job任务
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    ResponseDataEntity<Void> addQuartzJob(@RequestBody QuartzJobAddParam param);

    /**
     * 添加一个job任务
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    ResponseDataEntity<Void> deleteQuartzJob(@RequestBody QuartzJobDeleteParam param);

}
