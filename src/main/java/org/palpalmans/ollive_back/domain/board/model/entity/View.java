package org.palpalmans.ollive_back.domain.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "views")
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class View {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public View(Long id, Board board, Member member) {
        this.id = id;
        this.board = board;
        this.member = member;
    }

    public View(Board board, Member member) {
        this.board = board;
        this.member = member;
    }
}
