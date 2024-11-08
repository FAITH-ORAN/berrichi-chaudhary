package com.party.backend.service.impl;

import com.party.backend.dto.MessageDto;
import com.party.backend.entity.Message;
import com.party.backend.entity.User;
import com.party.backend.mapper.MessageMapper;
import com.party.backend.repository.MessageRepository;
import com.party.backend.repository.UserRepository;
import com.party.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public MessageDto sendMessage(MessageDto messageDto) {
        Message message = messageMapper.toEntity(messageDto);

        // Chargement des utilisateurs pour lier les entités User à Message
        User sender = userRepository.findById(messageDto.getFromUserId())
                .orElseThrow(() -> new RuntimeException("sender not found"));
        User receiver = userRepository.findById(messageDto.getToUserId())
                .orElseThrow(() -> new RuntimeException("receiver not found"));

        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSentAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        return messageMapper.toDto(savedMessage);
    }

    @Override
    public Page<MessageDto> getReceivedMessages(Long receiverId, Pageable pageable) {
        return messageRepository.findReceivedMessagesWithSenderPseudo(receiverId, pageable)
                .map(messageMapper::toDto);
    }

    @Override
    public Page<MessageDto> getSentMessages(Long senderId, Pageable pageable) {
        return messageRepository.findSentMessagesWithReceiverPseudo(senderId, pageable)
                .map(messageMapper::toDto);
    }

    @Override
    @CacheEvict(value = "receivedMessages", key = "#receiverId")
    public void deleteReceivedMessage(Long messageId, Long receiverId) {
        messageRepository.deleteReceivedMessage(messageId, receiverId);
    }

    @Override
    @CacheEvict(value = "sentMessages", key = "#senderId")
    public void deleteSentMessage(Long messageId, Long senderId) {
        messageRepository.deleteSentMessage(messageId, senderId);
    }
}
