package wang.ismy.push.connector.entity;

import lombok.Data;

/**
 * @author MY
 * @date 2020/10/5 20:13
 */
@Data
public class ResourceInfo {

    private String os;

    private Long totalMemory;

    private Long memoryAvailable;

}
