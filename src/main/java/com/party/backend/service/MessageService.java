package com.party.backend.service;

import com.party.backend.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    MessageDto sendMessage(MessageDto messageDto);
    Page<MessageDto> getReceivedMessages(Long receiverId, Pageable pageable);
    Page<MessageDto> getSentMessages(Long senderId, Pageable pageable);

    void deleteReceivedMessage(Long messageId, Long receiverId);

    void deleteSentMessage(Long messageId, Long senderId);
}
