package com.tuacy.actuator.indicator;

import com.tuacy.actuator.utils.FileSizeUtil;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 通过自定义一个类继承AbstractHealthIndicator，往health端点添加数据
 *
 * allDiskSpace2 对应的信息会在 http://127.0.0.1:2224/actuator/health 里面显示出来
 */
@Component("allDiskSpace2")
public class AllDiskSpaceAbstractHealthIndicator extends AbstractHealthIndicator {

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        File[] rootFiles = File.listRoots();
        if (rootFiles != null && rootFiles.length != 0) {
            long total = 0, free = 0;
            for (File file : rootFiles) {
                total += file.getTotalSpace(); // 总量
                free += file.getUsableSpace(); // 未用
            }
            long user = total - free; // 已用
            double userRate = total == 0 ? 0 : ((double) user / total);// 利用率
            builder.up()
                    .withDetail("diskspaceTotal", FileSizeUtil.getPrintSize(total))
                    .withDetail("diskspaceFree", FileSizeUtil.getPrintSize(free))
                    .withDetail("diskspaceUsage", String.valueOf(userRate * 100))
                    .build();
        } else {
            builder.down().build();
        }

    }
}
