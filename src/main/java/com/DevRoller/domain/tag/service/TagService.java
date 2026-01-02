package com.devroller.domain.tag.service;

import com.devroller.domain.tag.dto.TagResponse;
import com.devroller.domain.tag.entity.Tag;
import com.devroller.domain.tag.repository.TagRepository;
import com.devroller.global.exception.BusinessException;
import com.devroller.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    /**
     * 전체 태그 목록 조회
     */
    public List<TagResponse> getAllTags() {
        return tagRepository.findAllByOrderByNameAsc()
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
     * 태그 단건 조회
     */
    public TagResponse getTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));
        return TagResponse.from(tag);
    }

    /**
     * 태그 생성 (관리자)
     */
    @Transactional
    public TagResponse createTag(String name, String color) {
        Tag tag = Tag.builder()
                .name(name)
                .color(color)
                .build();
        tagRepository.save(tag);
        return TagResponse.from(tag);
    }

    /**
     * 태그 수정 (관리자)
     */
    @Transactional
    public TagResponse updateTag(Long tagId, String name, String color) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));
        tag.update(name, color);
        return TagResponse.from(tag);
    }

    /**
     * 태그 삭제 (관리자)
     */
    @Transactional
    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TAG_NOT_FOUND));
        tagRepository.delete(tag);
    }
}
