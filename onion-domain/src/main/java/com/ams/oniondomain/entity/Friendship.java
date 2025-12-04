package com.ams.oniondomain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        name = "friendships",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id_1", "user_id_2"})
        }
)
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 친구관계는 양방향이지만 DB에는 1 row
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_1", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_2", nullable = false)
    private User user2;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * 항상 userId 작은 쪽을 user1으로 설정해서 중복 방지
     */
    public static Friendship of(User a, User b) {
        if (a.getId() < b.getId()) {
            return Friendship.builder()
                    .user1(a)
                    .user2(b)
                    .build();
        } else {
            return Friendship.builder()
                    .user1(b)
                    .user2(a)
                    .build();
        }
    }
}
