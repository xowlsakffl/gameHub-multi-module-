package com.ams.onionuserapi.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collections;

@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private static final String ATTR_EMAIL = "ws_email";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object email = accessor.getSessionAttributes() != null ? accessor.getSessionAttributes().get(ATTR_EMAIL) : null;
            if (!(email instanceof String emailValue) || emailValue.isBlank()) {
                return null;
            }
            Principal principal = new UsernamePasswordAuthenticationToken(emailValue, null, Collections.emptyList());
            accessor.setUser(principal);
            return message;
        }

        if (StompCommand.SEND.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            if (accessor.getUser() == null) {
                return null;
            }
        }

        return message;
    }
}
