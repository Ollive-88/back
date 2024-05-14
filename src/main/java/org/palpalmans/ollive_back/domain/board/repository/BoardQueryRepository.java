package org.palpalmans.ollive_back.domain.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.domain.board.model.entity.Board;
import org.palpalmans.ollive_back.domain.board.model.entity.QBoard;
import org.palpalmans.ollive_back.domain.board.model.entity.QBoardTag;
import org.palpalmans.ollive_back.domain.board.model.entity.QTag;
import org.palpalmans.ollive_back.domain.member.model.entity.QMember;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private static final QBoard qBoard = QBoard.board;
    private static final QMember qMember = QMember.member;
    private static final QBoardTag qBoardTag = QBoardTag.boardTag;
    private static final QTag qTag = QTag.tag;

    @Transactional(readOnly = true)
    public List<Board> getBoardList(
            String keyword, Long lastIndex, int size, Long memberId,
            List<String> tagNames
    ) {
        if (tagNames.isEmpty()) {
            return jpaQueryFactory
                    .select(qBoard)
                    .from(qBoard)
                    .join(qBoard.member).fetchJoin()
                    .leftJoin(qBoard.boardTags).fetchJoin() //.fetchJoin()
                    .orderBy(qBoard.id.desc())
                    .where((boardListCondition(keyword, lastIndex, memberId, tagNames)))
                    .limit(size)
                    .fetch();
        }
        return jpaQueryFactory
                .select(qBoard)
                .from(qBoard)
                .join(qBoard.member).fetchJoin()
                .join(qBoardTag).on(qBoard.eq(qBoardTag.board)).fetchJoin()
                .join(qTag).on(qBoardTag.tag.eq(qTag).and(qTag.name.in(tagNames))).fetchJoin()
                .leftJoin(qBoard.comments).fetchJoin()
                .where(boardListCondition(keyword, lastIndex, memberId, tagNames))
                .orderBy(qBoard.id.desc())
                .limit(size)
                .fetch();
    }

    private static BooleanExpression boardListCondition(
            String keyword, Long lastIndex, Long memberId, List<String> tagNames
    ) {
        BooleanExpression indexCondition = lastIndex == 0 ? qBoard.id.gt(lastIndex) : qBoard.id.lt(lastIndex);
        return indexCondition
                .and(keyword == null ? null : qBoard.title.contains(keyword))
                .and(tagNames.isEmpty() ? null : qTag.name.in(tagNames))
                .and(memberId == 0 ? null : qBoard.member.id.eq(memberId));
    }
}
