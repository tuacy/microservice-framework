package com.tuacy.actuator.meter;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.File;

@Component
public class DiskMetrics implements MeterBinder {

    private File rootFilePath;

    public DiskMetrics() {
        this.rootFilePath = new File(".");
    }

    @Override
    public void bindTo(@NotNull MeterRegistry registry) {
        // 磁盘已用容量
        Gauge.builder("diskSpaceInfo.total", rootFilePath, File::getTotalSpace)
                .register(registry);
        // 磁盘剩余容量
        Gauge.builder("diskSpaceInfo.free", rootFilePath, File::getFreeSpace)
                .register(registry);
        // 磁盘使用率
        Gauge.builder("diskSpaceInfo.usage", rootFilePath, c -> {
            long totalDiskSpace = rootFilePath.getTotalSpace();
            if (totalDiskSpace == 0) {
                return 0.0;
            }

            long usedDiskSpace = totalDiskSpace - rootFilePath.getFreeSpace();
            return (double) usedDiskSpace / totalDiskSpace * 100;
        })
                .register(registry);
    }
}
