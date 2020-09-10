package wang.ismy.push.client;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class ConnectorTest {

    @Test
    public void lookupConnectorExpectSuccess() throws IOException, InterruptedException {
        HttpTemplate httpTemplate = mock(HttpTemplate.class);
        String lookupAddress = "testLookup.com";
        String connectorStr = "192.168.1.100:8888";
        when(httpTemplate.get(eq(lookupAddress))).thenReturn(connectorStr);

        Connector connector = new Connector(lookupAddress, httpTemplate);
        assertTrue(connector.lookupConnector());
    }

    @Test
    public void lookupConnectorExceptFail() throws IOException, InterruptedException {
        HttpTemplate httpTemplate = mock(HttpTemplate.class);
        String lookupAddress = "testLookup.com";
        String connectorStr = "192.168.1.100";
        when(httpTemplate.get(eq(lookupAddress))).thenReturn(connectorStr);

        Connector connector = new Connector(lookupAddress, httpTemplate);
        assertFalse(connector.lookupConnector());
    }

    @Test
    public void isAvailableExceptNo(){
        Connector connector = new Connector("test.url");
        assertFalse(connector.isAvailable());
    }

    @Test
    public void isAvailableExceptYes() throws NoSuchFieldException, IllegalAccessException, IOException, InterruptedException {
        HttpTemplate httpTemplate = mock(HttpTemplate.class);
        String lookupAddress = "testLookup.com";
        String connectorStr = "192.168.1.100:8888";
        when(httpTemplate.get(eq(lookupAddress))).thenReturn(connectorStr);

        Connector connector = new Connector(lookupAddress,httpTemplate);
        connector.lookupConnector();

        assertTrue(connector.isAvailable());
    }
}