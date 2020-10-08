package wang.ismy.push.client.factory;

import wang.ismy.push.client.Connector;
import wang.ismy.push.client.auth.AuthManager;
import wang.ismy.push.client.auth.AuthRequest;

public class ConnectorFactory {

    public static Connector newConnector(String gateway, AuthManager authManager){
        return new Connector(gateway, authManager);
    }
}
