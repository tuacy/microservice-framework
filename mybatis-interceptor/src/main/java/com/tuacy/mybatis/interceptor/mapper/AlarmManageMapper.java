package com.tuacy.mybatis.interceptor.mapper;

import com.tuacy.mybatis.interceptor.entity.param.AlarmInfoInsetParam;
import com.tuacy.mybatis.interceptor.interceptor.tableshard.TableShardAnnotation;
import com.tuacy.mybatis.interceptor.strategy.AlarmTableShardStrategy;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 告警管理mapper
 */
@Repository
public interface AlarmManageMapper {

    @TableShardAnnotation(tableName = "tablealarmevent", shadeStrategy = AlarmTableShardStrategy.class, shardParamKey = "occurTime")
    long insertAlarm(@Param("param") AlarmInfoInsetParam param, @Param("occurTime") String occurTIme);

}
