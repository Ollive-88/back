package org.palpalmans.ollive_back.domain.board.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.palpalmans.ollive_back.domain.board.model.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("Tag Repository write test")
    @Transactional
    void tagRepositoryTest() {
        Tag tag = tagRepository.save(new Tag("tagName"));
        List<Tag> tags = tagRepository.findAll();
        Tag foundTag = tags.get(0);
        assertThat(tags.size()).isEqualTo(1);
        assertThat(foundTag)
                .usingRecursiveAssertion()
                .isEqualTo(tag);
    }
}