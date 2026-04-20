package com.ams.oniondomain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "party_chat_read",
        uniqueConstraints = @UniqueConstraint(name = "uk_party_chat_read_party_user", columnNames = {"party_id", "user_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyChatRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private GameParty party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "last_read_message_id")
    private Long lastReadMessageId;

    @Column(name = "read_at", nullable = false)
    private LocalDateTime readAt;

    @Builder
    public PartyChatRead(GameParty party, User user, Long lastReadMessageId) {
        this.party = party;
        this.user = user;
        this.lastReadMessageId = lastReadMessageId;
    }

    @PrePersist
    protected void onCreate() {
        this.readAt = LocalDateTime.now();
    }

    public void markRead(Long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
        this.readAt = LocalDateTime.now();
    }
}
