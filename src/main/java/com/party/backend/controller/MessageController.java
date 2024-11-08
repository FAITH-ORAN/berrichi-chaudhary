package com.party.backend.controller;

import com.party.backend.dto.MessageDto;
import com.party.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "Envoyer un message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message envoyé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping("/send")
    public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageDto messageDto) {
        MessageDto savedMessage = messageService.sendMessage(messageDto);
        return ResponseEntity.ok(savedMessage);
    }

    @Operation(summary = "Obtenir les messages reçus par l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages reçus avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun message trouvé pour cet utilisateur")
    })
    @GetMapping("/received/{receiverId}")
    public ResponseEntity<Page<MessageDto>> getReceivedMessages(@PathVariable Long receiverId, Pageable pageable) {
        Page<MessageDto> messages = messageService.getReceivedMessages(receiverId, pageable);
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Obtenir les messages envoyés par l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages envoyés avec succès"),
            @ApiResponse(responseCode = "404", description = "Aucun message trouvé pour cet utilisateur")
    })
    @GetMapping("/sent/{senderId}")
    public ResponseEntity<Page<MessageDto>> getSentMessages(@PathVariable Long senderId, Pageable pageable) {
        Page<MessageDto> messages = messageService.getSentMessages(senderId, pageable);
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Supprimer un message reçu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Message reçu supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Message non trouvé")
    })
    @DeleteMapping("/received/{receiverId}/message/{messageId}")
    public ResponseEntity<Void> deleteReceivedMessage(
            @PathVariable Long receiverId,
            @PathVariable Long messageId) {
        messageService.deleteReceivedMessage(messageId, receiverId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Supprimer un message envoyé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Message envoyé supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Message non trouvé")
    })
    @DeleteMapping("/sent/{senderId}/message/{messageId}")
    public ResponseEntity<Void> deleteSentMessage(
            @PathVariable Long senderId,
            @PathVariable Long messageId) {
        messageService.deleteSentMessage(messageId, senderId);
        return ResponseEntity.noContent().build();
    }
}
