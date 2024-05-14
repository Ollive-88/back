package org.palpalmans.ollive_back.domain.board.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.palpalmans.ollive_back.domain.board.model.entity.Board;
import org.palpalmans.ollive_back.domain.board.model.entity.Like;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.status.MemberRole;
import org.palpalmans.ollive_back.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class LikeRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Like Save Test")
    @Transactional
    void saveLike() {
        Member member = Member.builder()
                .name("name")
                .nickname("nickname")
                .email("email")
                .birthday(new Date())
                .gender("gender")
                .role(MemberRole.ROLE_REGISTERED_MEMBER)
                .build();

        Board board = new Board("title", "content", member);

        memberRepository.save(member);
        boardRepository.save(board);

        Like like = likeRepository.save(new Like(board, member));
        List<Like> likes = likeRepository.findAll();
        Like foundLike = likes.get(0);

        assertThat(likes.size()).isEqualTo(1);
        assertThat(foundLike).usingRecursiveAssertion().isEqualTo(like);
    }
}