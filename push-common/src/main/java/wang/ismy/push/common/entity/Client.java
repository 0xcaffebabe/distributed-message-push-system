package wang.ismy.push.common.entity;

import lombok.Data;

import java.net.InetAddress;

/**
 * @author MY
 * @date 2020/6/16 21:00
 */
@Data
public class Client {
    private String userId;
    private InetAddress inetAddress;
}
