package com.party.backend.repository;

import com.party.backend.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  @Query("SELECT m FROM Message m JOIN FETCH m.sender sender JOIN FETCH m.receiver receiver WHERE m.receiver.id = :receiverId")
  Page<Message> findReceivedMessagesWithSenderPseudo(@Param("receiverId") Long receiverId, Pageable pageable);

  @Query("SELECT m FROM Message m JOIN FETCH m.sender sender JOIN FETCH m.receiver receiver WHERE m.sender.id = :senderId")
  Page<Message> findSentMessagesWithReceiverPseudo(@Param("senderId") Long senderId, Pageable pageable);
    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.id = :messageId AND m.receiver.id = :receiverId")
    void deleteReceivedMessage(@Param("messageId") Long messageId, @Param("receiverId") Long receiverId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.id = :messageId AND m.sender.id = :senderId")
    void deleteSentMessage(@Param("messageId") Long messageId, @Param("senderId") Long senderId);

}
