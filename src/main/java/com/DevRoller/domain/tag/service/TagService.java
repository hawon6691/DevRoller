package com.devroller.domain.tag.service;

import com.devroller.domain.tag.dto.TagRequest;
import com.devroller.domain.tag.dto.TagResponse;
import com.devroller.domain.tag.entity.Tag;
import com.devroller.domain.tag.repository.TagRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 태그 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    /**
     * 태그 조회 (ID)
     */
    public Tag findById(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));
    }

    /**
     * 전체 태그 목록
     */
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 태그 검색
     */
    public List<TagResponse> searchTags(String keyword) {
        return tagRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 인기 태그 조회
     */
    public List<TagResponse> getPopularTags(int limit) {
        return tagRepository.findPopularTags(PageRequest.of(0, limit))
                .stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 태그 생성
     */
    @Transactional
    public TagResponse createTag(TagRequest request) {
        if (tagRepository.existsByName(request.getName())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 태그명입니다.");
        }

        Tag tag = Tag.builder()
                .name(request.getName())
                .color(request.getColor())
                .build();

        Tag savedTag = tagRepository.save(tag);
        log.info("Tag created: {}", savedTag.getName());

        return TagResponse.from(savedTag);
    }

    /**
     * 태그 수정
     */
    @Transactional
    public TagResponse updateTag(Long tagId, TagRequest request) {
        Tag tag = findById(tagId);

        if (request.getName() != null && !request.getName().equals(tag.getName())) {
            if (tagRepository.existsByName(request.getName())) {
                throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 태그명입니다.");
            }
        }

        tag.update(request.getName(), request.getColor());
        log.info("Tag updated: {}", tagId);

        return TagResponse.from(tag);
    }

    /**
     * 태그 삭제
     */
    @Transactional
    public void deleteTag(Long tagId) {
        Tag tag = findById(tagId);
        tagRepository.delete(tag);
        log.info("Tag deleted: {}", tagId);
    }
}