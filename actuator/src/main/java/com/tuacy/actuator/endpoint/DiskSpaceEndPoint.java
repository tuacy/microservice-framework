package com.tuacy.actuator.endpoint;

import com.tuacy.actuator.utils.FileSizeUtil;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义一个端点 id = diskspace 获取磁盘容量信息
 */
@Configuration
@Endpoint(id = "diskspace") // @EndPoint中的id不能使用驼峰法，需要以-分割
public class DiskSpaceEndPoint {

    /**
     * 获取自定义端点需要监测的数据 -- 磁盘容量信息
     *
     * @return Map<String, String>
     */
    @ReadOperation
    public Map<String, String> diskSpaceInfo() {
        Map<String, String> result = new HashMap<>();
        // 获取磁盘容量信息
        File[] rootFiles = File.listRoots();
        if (rootFiles != null && rootFiles.length != 0) {
            long total = 0;
            long free = 0;
            for (File file : rootFiles) {
                total += file.getTotalSpace(); // 总量
                free += file.getUsableSpace(); // 未用
            }
            long user = total - free; // 已用
            double userRate = total == 0 ? 0 : ((double) user / total);// 利用率
            result.put("diskspaceTotal", FileSizeUtil.getPrintSize(total));
            result.put("diskspaceFree", FileSizeUtil.getPrintSize(free));
            result.put("diskspaceUsage", String.valueOf(userRate * 100));
        }
        return result;
    }

}
