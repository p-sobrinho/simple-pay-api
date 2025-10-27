package me.koji.simplepaymentapi.services.contracts;

import me.koji.simplepaymentapi.models.ClientUser;

public interface NotificationService {
    boolean sendNotification(ClientUser user, String message);
}
