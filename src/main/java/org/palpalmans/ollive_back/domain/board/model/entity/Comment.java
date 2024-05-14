package org.palpalmans.ollive_back.domain.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.palpalmans.ollive_back.common.BaseTimeEntity;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public Comment(Long id, String content, Board board, Member member) {
        this.id = id;
        this.content = content;
        this.board = board;
        this.member = member;
    }

    public Comment(String content, Board board, Member member) {
        this.content = content;
        this.board = board;
        this.member = member;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", boardId=" + board.getId() +
                ", memberId=" + member.getId() +
                '}';
    }
}
