package wang.ismy.push.client.factory;

import org.junit.Test;
import wang.ismy.push.client.Connector;

import static org.junit.Assert.*;

public class ConnectorFactoryTest {

    @Test
    public void newConnector() {
        Connector connector = ConnectorFactory.newConnector("http://test.url");
        assertNotNull(connector);
    }
}