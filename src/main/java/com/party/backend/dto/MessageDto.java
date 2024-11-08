package com.party.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {
        private Long id;
        private String content;
        private LocalDateTime sentAt;
        private Long fromUserId;
        private Long toUserId;
        private String senderPseudo;
        private String receiverPseudo;
}
