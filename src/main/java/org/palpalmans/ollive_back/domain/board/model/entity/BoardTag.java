package org.palpalmans.ollive_back.domain.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.palpalmans.ollive_back.common.BaseTimeEntity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class BoardTag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public BoardTag(long id, Board board, Tag tag) {
        this.id = id;
        this.board = board;
        this.tag = tag;
    }

    public BoardTag(Board board, Tag tag) {
        this.board = board;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "BoardTag{" +
                "id=" + id +
                ", boardId=" + board.getId() +
                ", tag=" + tag +
                '}';
    }
}
