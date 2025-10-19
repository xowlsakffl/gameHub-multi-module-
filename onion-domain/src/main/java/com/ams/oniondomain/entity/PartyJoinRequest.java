package com.ams.oniondomain.entity;

import com.ams.oniondomain.entity.enums.JoinRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "party_join_request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyJoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private GameParty party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User requester;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JoinRequestStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public PartyJoinRequest(GameParty party, User requester, String message, JoinRequestStatus status) {
        this.party = party;
        this.requester = requester;
        this.message = message;
        this.status = status != null ? status : JoinRequestStatus.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = JoinRequestStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void approve() {
        this.status = JoinRequestStatus.APPROVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = JoinRequestStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }
}
