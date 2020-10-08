package wang.ismy.push.client;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author MY
 * @date 2020/10/8 20:59
 */
@Data
@EqualsAndHashCode
public class ConnectorDTO {
    private String host;
    private Integer port;
    private String area;

    public static ConnectorDTO emptyConnector(){
        ConnectorDTO connector = new ConnectorDTO();
        connector.setArea("default");
        connector.setHost("");
        connector.setPort(-1);
        return connector;
    }
}
