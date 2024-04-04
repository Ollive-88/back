package org.palpalmans.ollive_back.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.palpalmans.ollive_back.domain.board.model.entity.Board;
import org.palpalmans.ollive_back.domain.board.model.entity.BoardTag;
import org.palpalmans.ollive_back.domain.board.model.entity.Comment;
import org.palpalmans.ollive_back.domain.board.model.entity.Tag;
import org.palpalmans.ollive_back.domain.board.repository.*;
import org.palpalmans.ollive_back.domain.member.model.entity.Member;
import org.palpalmans.ollive_back.domain.member.model.entity.NormalMember;
import org.palpalmans.ollive_back.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static org.palpalmans.ollive_back.domain.member.model.status.MemberRole.ROLE_ADMIN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    BoardTagRepository boardTagRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    ViewRepository viewRepository;

    Member member;
    NormalMember normalMember;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("test@naver.com")
                .name("name")
                .nickname("nickname")
                .birthday(new Date())
                .profilePicture("profilePicture")
                .role(ROLE_ADMIN)
                .gender("male")
                .build();

        normalMember = new NormalMember(member, "1234");
        memberRepository.save(normalMember);
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("write board controller test")
    @Transactional
    void writeBoard() throws Exception {
        MockMultipartFile mockImage1 = new MockMultipartFile("images", "test1".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile mockImage2 = new MockMultipartFile("images", "test2".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/api/v1/boards")
                        .file(mockImage1)
                        .file(mockImage2)
                        .content("content")
                        .param("title", "title")
                        .param("content", "content")
                        .param("tagNames", "tagName1")
                        .param("tagNames", "tagName2")
                )
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Transactional
    void getBoardsSetup(List<Board> boardList, List<Tag> tagList) {
        // when
        Board board1 = boardRepository.save(new Board("title1 hello", "content1", 1L));
        Board board2 = boardRepository.save(new Board("title2 hi", "content2", 1L));
        Board board3 = boardRepository.save(new Board("title3 nice", "content3", 1L));
        Board board4 = boardRepository.save(new Board("title4 to meet you", "content4", 1L));
        boardList.add(board1);
        boardList.add(board2);
        boardList.add(board3);
        boardList.add(board4);

        Tag tag1 = tagRepository.save(new Tag("tagName1"));
        Tag tag2 = tagRepository.save(new Tag("tagName2"));
        Tag tag3 = tagRepository.save(new Tag("tagName3"));
        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);

        boardTagRepository.save(new BoardTag(board1, tag1));
        boardTagRepository.save(new BoardTag(board1, tag2));
        boardTagRepository.save(new BoardTag(board2, tag2));
        boardTagRepository.save(new BoardTag(board2, tag3));
        boardTagRepository.save(new BoardTag(board3, tag1));
        boardTagRepository.save(new BoardTag(board3, tag3));
    }

    @Transactional
    void getBoardsTearDown(List<Board> boardList, List<Tag> tagList) {
        boardTagRepository.deleteAll();

        boardRepository.deleteAll();
        boardList.clear();

        tagRepository.deleteAll();
        tagList.clear();
    }

    @DisplayName("get boards test")
    @CsvSource(value = {
            "null,0,5,tagName1,tagName2,0",
            "null,0,5,tagName2,tagName3,1",
            "null,0,5,tagName1,tagName3,2",
    }, delimiter = ',')
    @ParameterizedTest
    @Transactional(propagation = SUPPORTS)
    void getBoardsTest(
            String keyword, String lastIndex, String size, String tag1, String tag2,
            String resultIdx1
    ) throws Exception {
        //when
        List<Board> boardList = new ArrayList<>();
        List<Tag> tagList = new ArrayList<>();
        getBoardsSetup(boardList, tagList);

        Board board1 = boardList.get(Integer.parseInt(resultIdx1));

        //given
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

        if (!"null".equals(keyword))
            requestParams.add("keyword", keyword);
        requestParams.add("lastIndex", lastIndex);
        requestParams.add("size", size);
        requestParams.add("tags", tag1);
        requestParams.add("tags", tag2);

        // then
        mockMvc.perform(get("/api/v1/boards")
                        .params(requestParams)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.boards").isArray())
                .andExpect(jsonPath("$.boards[0].title").value(board1.getTitle()))
                .andExpect(jsonPath("$.boards[0].content").value(board1.getContent()))
                .andExpect(jsonPath("$.boards[0].tags").isArray())
                .andExpect(jsonPath("$.boards[0].tags[*]", Matchers.hasItems(tag1, tag2)))
                .andDo(print());

        getBoardsTearDown(boardList, tagList);
    }

    @Transactional
    void getBoardDetailSetup(Board board, List<Comment> comments) {
        board = boardRepository.save(board);

        Comment comment1 = new Comment("content1", board, normalMember);
        Comment comment2 = new Comment("content2", board, normalMember);
        Comment comment3 = new Comment("content3", board, normalMember);
        Comment comment4 = new Comment("content4", board, normalMember);
        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);
        comments.add(comment4);
        commentRepository.saveAll(comments);
    }

    @Test
    @DisplayName("board detail controller success test")
    @Transactional(propagation = SUPPORTS)
    @WithUserDetails(value = "test@naver.com",
            userDetailsServiceBeanName = "customMemberDetailsService",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void getBoardDetail() throws Exception {
        // when
        Board savedBoard = new Board("title", "content", 1L);
        List<Comment> comments = new ArrayList<>();
        getBoardDetailSetup(savedBoard, comments);
        comments.sort(Comparator.comparing(Comment::getCreatedAt));

        // given
        mockMvc.perform(get("/api/v1/boards/" + savedBoard.getId())
                        .contentType(APPLICATION_JSON)
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(savedBoard.getTitle()))
                .andExpect(jsonPath("$.content").value(savedBoard.getContent()))
                .andExpect(jsonPath("$.likeCount").value(0))
                .andExpect(jsonPath("$.viewCount").value(0))
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments[0].commentId").value(comments.get(0).getId()))
                .andExpect(jsonPath("$.comments[1].commentId").value(comments.get(1).getId()))
                .andExpect(jsonPath("$.comments[2].commentId").value(comments.get(2).getId()))
                .andExpect(jsonPath("$.comments[3].commentId").value(comments.get(3).getId()))
                .andDo(print());
    }
}