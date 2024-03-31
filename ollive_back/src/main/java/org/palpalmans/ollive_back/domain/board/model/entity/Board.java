package org.palpalmans.ollive_back.domain.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.palpalmans.ollive_back.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Long memberId;

    @OneToMany(mappedBy = "board", cascade = PERSIST, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = PERSIST, orphanRemoval = true)
    private final List<BoardTag> boardTags = new ArrayList<>();

    public Board(Long id, String title, Long memberId, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }

    public Board(String title, String content, Long memberId) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + (content.length() < 7 ? content : content.substring(0, 7) + "...") + '\'' +
                ", memberId=" + memberId +
                ", comments=" + comments +
                ", boardTags=" + boardTags +
                '}';
    }
}
