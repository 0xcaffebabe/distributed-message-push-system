package wang.ismy.push.client.factory;

import wang.ismy.push.client.Connector;

public class ConnectorFactory {

    public static Connector newConnector(String lookupAddress){
        return new Connector(lookupAddress);
    }
}
