package com.tuacy.actuator.indicator;

import com.tuacy.actuator.utils.FileSizeUtil;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 通过自定义一个类实现HealthIndicator接口，往health端点添加数据
 * allDiskSpace0 对应的信息会在 http://127.0.0.1:2224/actuator/health 里面显示出来
 */
@Component("allDiskSpace0")
public class AllDiskSpaceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        File[] rootFiles = File.listRoots();
        if (rootFiles != null && rootFiles.length != 0) {
            long total = 0, free = 0;
            for (File file : rootFiles) {
                total += file.getTotalSpace(); // 总量
                free += file.getUsableSpace(); // 未用
            }
            long user = total - free; // 已用
            double userRate = total == 0 ? 0 : ((double) user / total);// 利用率
            return Health.up()
                    .withDetail("diskspaceTotal", FileSizeUtil.getPrintSize(total))
                    .withDetail("diskspaceFree", FileSizeUtil.getPrintSize(free))
                    .withDetail("diskspaceUsage", String.valueOf(userRate * 100))
                    .build();
        } else {
            return Health.down().build();
        }
    }
}
