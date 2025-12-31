package com.devroller.domain.category.repository;

import com.devroller.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 카테고리 Repository
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 이름으로 카테고리 조회
    Optional<Category> findByName(String name);

    // 이름 존재 여부
    boolean existsByName(String name);

    // 활성 카테고리만 조회 (정렬 순서대로)
    List<Category> findByIsActiveTrueOrderByDisplayOrderAsc();

    // 모든 카테고리 (정렬 순서대로)
    List<Category> findAllByOrderByDisplayOrderAsc();
}