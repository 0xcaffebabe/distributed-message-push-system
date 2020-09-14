package wang.ismy.push.client.factory;

import wang.ismy.push.client.bio.BioClient;
import wang.ismy.push.client.Client;

public class ClientFactory {

    public static Client newBioClient(String clientId){
        return new BioClient(clientId);
    }
}
