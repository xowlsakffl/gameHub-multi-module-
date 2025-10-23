package com.ams.oniondomain.entity;

import com.ams.oniondomain.entity.enums.PartyRole;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "party_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private GameParty party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** 🟢 역할 컬럼 추가 (LEADER / MEMBER) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public PartyMember(GameParty party, User user, PartyRole role, LocalDateTime joinedAt) {
        this.party = party;
        this.user = user;
        this.role = role != null ? role : PartyRole.MEMBER; // 기본은 일반 멤버
        this.joinedAt = joinedAt != null ? joinedAt : LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.joinedAt == null) this.joinedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /** 🟡 역할 변경 (방장 위임 시 사용) */
    public void changeRole(PartyRole newRole) {
        this.role = newRole;
        this.updatedAt = LocalDateTime.now();
    }
}
