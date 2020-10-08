package wang.ismy.push.admin.entity;

import lombok.Data;

/**
 * @author MY
 * @date 2020/9/27 21:11
 */
@Data
public class ConnectorDTO {
    private String name;
    private Boolean reachable;
    private String state;
    private Long users;
    private Integer delay;
}
