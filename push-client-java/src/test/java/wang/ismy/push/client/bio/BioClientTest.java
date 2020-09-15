package wang.ismy.push.client.bio;

import org.junit.Test;
import org.mockito.internal.verification.Times;
import org.mockito.verification.VerificationMode;
import wang.ismy.push.client.Connector;

import java.net.ConnectException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
public class BioClientTest {

    @Test
    public void connectFail() throws Exception {
        String userId = "9527";
        BioClient client = new BioClient(userId);
        Connector connector = mock(Connector.class);

        when(connector.lookupConnector()).thenThrow(new ConnectException("假装网络异常"));

        client.connect(connector);

    }
}