package com.ams.oniondomain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_party")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String gameName;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = PartyStatus.OPEN;
        if (currentPlayers == 0) currentPlayers = 1;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void update(String title, String gameName, int maxPlayer, String description) {
        this.title = title;
        this.gameName = gameName;
        this.maxPlayer = maxPlayer;
        this.description = description;
    }

    public enum PartyStatus {
        OPEN, FULL, CLOSED
    }
}
