package wang.ismy.push.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author MY
 * @date 2020/9/28 20:44
 */
@Data
public class ClientDTO {
    private String id;

    /**
     * 所属connector实例
     */
    private String connector;
    /**
     * 最后活跃时间(最后一通信时间)
     */
    private LocalDateTime lastActive;

    public static ClientDTO emptyClient(){
        ClientDTO client = new ClientDTO();
        client.connector = "unknow";
        client.lastActive = LocalDateTime.of(1999,2,17,0,0);
        client.id = "-1";
        return client;
    }
}
