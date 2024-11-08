package com.party.backend.mapper;

import com.party.backend.dto.MessageDto;
import com.party.backend.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "sender.id", target = "fromUserId")
    @Mapping(source = "receiver.id", target = "toUserId")
    @Mapping(source = "sender.pseudo", target = "senderPseudo")
    @Mapping(source = "receiver.pseudo", target = "receiverPseudo")
    MessageDto toDto(Message message);

    @Mapping(source = "fromUserId", target = "sender.id")
    @Mapping(source = "toUserId", target = "receiver.id")
    Message toEntity(MessageDto messageDto);
}

