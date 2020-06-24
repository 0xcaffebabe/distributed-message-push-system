package wang.ismy.push.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author MY
 * @date 2020/6/16 21:02
 */
@Data
public class Message implements Serializable {
    private byte[] payload;
    private Integer to;
}
