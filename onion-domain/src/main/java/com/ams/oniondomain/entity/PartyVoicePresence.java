package com.ams.oniondomain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "party_voice_presence",
        uniqueConstraints = @UniqueConstraint(name = "uk_party_voice_presence_party_user", columnNames = {"party_id", "user_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyVoicePresence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private GameParty party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "channel_name", nullable = false, length = 50)
    private String channelName;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public PartyVoicePresence(GameParty party, User user, String channelName) {
        this.party = party;
        this.user = user;
        this.channelName = channelName;
        this.active = true;
    }

    @PrePersist
    protected void onCreate() {
        this.joinedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }

    public void joinOrSwitch(String channelName) {
        this.channelName = channelName;
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void leave() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }
}
