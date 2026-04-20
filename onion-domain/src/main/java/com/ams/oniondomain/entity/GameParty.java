package com.ams.oniondomain.entity;

import com.ams.oniondomain.entity.enums.PartyStatus;
import com.ams.oniondomain.entity.enums.PartyType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.security.SecureRandom;

@Entity
@Table(name = "game_party")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameParty {
    private static final String INVITE_CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int INVITE_CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String gameName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyType type;

    @Column(nullable = false)
    private int maxPlayer;

    @Column(nullable = false)
    private int currentPlayers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyStatus status;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "invite_code", nullable = false, unique = true, length = 16)
    private String inviteCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Builder
    public GameParty(String title, String gameName, PartyType type, int maxPlayer, String description, User creator, PartyStatus status) {
        this.title = title;
        this.gameName = gameName;
        this.type = type != null ? type : PartyType.AUTO_JOIN;
        this.maxPlayer = maxPlayer;
        this.currentPlayers = 1;
        this.description = description;
        this.creator = creator;
        this.status = PartyStatus.OPEN;
        this.inviteCode = generateInviteCode();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = PartyStatus.OPEN;
        if (currentPlayers == 0) currentPlayers = 1;
        if (inviteCode == null || inviteCode.isBlank()) inviteCode = generateInviteCode();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void update(String title, String gameName, int maxPlayer, String description, PartyType type) {
        if (title != null) this.title = title;
        if (gameName != null) this.gameName = gameName;
        if (maxPlayer > 0) this.maxPlayer = maxPlayer;
        if (description != null) this.description = description;
        if (type != null) this.type = type;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeStatus(PartyStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementPlayers() {
        this.currentPlayers++;
    }

    public void decrementPlayers() {
        if (this.currentPlayers > 0) this.currentPlayers--;
    }

    public void regenerateInviteCode() {
        this.inviteCode = generateInviteCode();
        this.updatedAt = LocalDateTime.now();
    }

    private String generateInviteCode() {
        StringBuilder builder = new StringBuilder(INVITE_CODE_LENGTH);
        for (int i = 0; i < INVITE_CODE_LENGTH; i++) {
            builder.append(INVITE_CODE_CHARS.charAt(RANDOM.nextInt(INVITE_CODE_CHARS.length())));
        }
        return builder.toString();
    }
}
