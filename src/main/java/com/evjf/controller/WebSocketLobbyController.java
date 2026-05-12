package com.evjf.controller;

import com.evjf.dto.GetLobbyDTO;
import com.evjf.service.LobbyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketLobbyController {

    // ATTRIBUTES

    private final SimpMessagingTemplate messagingTemplate;
    private final LobbyService lobbyService;
    private static final Logger log = LoggerFactory.getLogger(WebSocketLobbyController.class);


    // CONSTRUCTORS

    public WebSocketLobbyController(SimpMessagingTemplate messagingTemplate,
                                    LobbyService lobbyService) {
        this.messagingTemplate = messagingTemplate;
        this.lobbyService = lobbyService;
    }

    // METHODS

    @MessageMapping("/lobby/{code}")
    public void notifyLobby(@DestinationVariable String code) {
        GetLobbyDTO lobby = lobbyService.getLobbyByCode(code);
        log.info("Message reçu pour le lobby : {}", code);
        messagingTemplate.convertAndSend("/topic/lobby/" + code, lobby);
    }
}