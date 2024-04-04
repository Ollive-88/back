package org.palpalmans.ollive_back.domain.board.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.palpalmans.ollive_back.domain.board.model.entity.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("Board Repository write test")
    @Transactional
    void boardRepositoryTest() {
        Board board = boardRepository.save(new Board("title", "content", 1L));
        List<Board> boards = boardRepository.findAll();
        Board foundBoard = boards.get(0);

        Assertions.assertThat(boards.size()).isEqualTo(1L);
        Assertions.assertThat(foundBoard).usingRecursiveAssertion().isEqualTo(board);
    }
}