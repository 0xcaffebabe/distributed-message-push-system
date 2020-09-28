package wang.ismy.push.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import wang.ismy.push.admin.entity.ClientDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MY
 * @date 2020/9/28 20:44
 */
@Service
@AllArgsConstructor
public class ClientService {
    private final RedisService redisService;

    public List<ClientDTO> getClients(int page, int length){
        if (page < 1) {
            page = 1;
        }
        int limit = (page-1) * length;
        var clientSet = redisService.getAllClient(limit, length);

        return clientSet.stream()
                .map(s->{
                    String[] t = s.split("-");
                    if (t.length != 3){
                        return ClientDTO.emptyClient();
                    }
                    ClientDTO client = new ClientDTO();
                    client.setId(t[2]);
                    client.setConnector("connector" + t[1]);
                    int ttl = redisService.getTTL(s);
                    client.setLastActive(calcLastActive(ttl));
                    return client;
                })
                .collect(Collectors.toList());
    }

    private LocalDateTime calcLastActive(int ttl){
        if (ttl < 0) {
            return LocalDateTime.of(1999,2,17,0,0);
        }
        return LocalDateTime.now().minusSeconds(ttl - 30);
    }
}
