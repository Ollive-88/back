package org.palpalmans.ollive_back.domain.cloth.respository;

import org.palpalmans.ollive_back.domain.cloth.model.entity.Cloth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface ClothRepository extends JpaRepository<Cloth, Long> {
    List<Cloth> findClothesBySuperCategory(String superCategory);
}