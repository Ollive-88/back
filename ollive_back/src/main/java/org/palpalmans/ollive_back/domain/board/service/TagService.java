package org.palpalmans.ollive_back.domain.board.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.palpalmans.ollive_back.domain.board.model.entity.Tag;
import org.palpalmans.ollive_back.domain.board.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    @PersistenceContext
    private EntityManager entityManager;

    private final TagRepository tagRepository;

    @Transactional
    public void saveTags(List<String> tagNames, List<Tag> savedTag) {
        List<Tag> tags = tagNames.stream()
                .map(Tag::new)
                .toList();

        Set<String> existTagNames = tagRepository.findAllByNameIn(
                        tags.stream()
                                .map(Tag::getName)
                                .toList()
                )
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        List<Tag> beSaved = tags.stream()
                .filter(tag -> !existTagNames.contains(tag.getName()))
                .toList();

        for (Tag tag : beSaved) {
            entityManager.persist(tag);
            savedTag.add(tag);
        }
        entityManager.flush();
        entityManager.clear();
    }
}
