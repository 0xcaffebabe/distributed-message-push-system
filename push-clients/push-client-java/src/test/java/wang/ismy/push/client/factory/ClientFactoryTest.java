package wang.ismy.push.client.factory;

import org.junit.Test;
import wang.ismy.push.client.bio.BioClient;
import wang.ismy.push.client.Client;

import static org.junit.Assert.*;

public class ClientFactoryTest {

    @Test
    public void newBioClient() {
        Client client = ClientFactory.newBioClient("test");
        assertNotNull(client);
        assertTrue(client instanceof BioClient);
    }
}