package me.koji.simplepaymentapi.services;

import lombok.extern.slf4j.Slf4j;
import me.koji.simplepaymentapi.dto.ClientNotificationDTO;
import me.koji.simplepaymentapi.mappers.UserMapper;
import me.koji.simplepaymentapi.models.ClientUser;
import me.koji.simplepaymentapi.services.contracts.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final RestTemplate restTemplate;
    private final UserMapper  userMapper;
    private final String notificationURL = "https://util.devi.tools/api/v1/notify";

    public NotificationServiceImpl(RestTemplate restTemplate, UserMapper userMapper) {
        this.restTemplate = restTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public boolean sendNotification(ClientUser user, String message) {
        try {
            // In comment since the api mock seems like is not working
            /*ResponseEntity<String> response = restTemplate.postForEntity(notificationURL,
                    new ClientNotificationDTO(userMapper.toDTO(user), message),
                    String.class
            );

            return response.getStatusCode().is2xxSuccessful();*/

            return true;
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);

            return false;
        }
    }
}
