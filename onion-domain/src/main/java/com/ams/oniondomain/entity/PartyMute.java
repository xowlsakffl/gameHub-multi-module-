package com.ams.oniondomain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "party_mute")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyMute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private GameParty party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "muted_by_user_id", nullable = false)
    private User mutedBy;

    @Column(length = 255)
    private String reason;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public PartyMute(GameParty party, User targetUser, User mutedBy, String reason, LocalDateTime expiresAt) {
        this.party = party;
        this.targetUser = targetUser;
        this.mutedBy = mutedBy;
        this.reason = reason;
        this.expiresAt = expiresAt;
        this.active = true;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }

    public void unmute() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isCurrentlyActive() {
        if (!active) {
            return false;
        }
        return expiresAt == null || expiresAt.isAfter(LocalDateTime.now());
    }
}
