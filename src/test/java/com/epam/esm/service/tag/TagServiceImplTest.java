package com.epam.esm.service.tag;

import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.mapper.tag.TagModelMapper;
import com.epam.esm.repository.tag.TagRepoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagRepoImpl tagRepo;

    @Mock
    private TagModelMapper modelMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;
    private TagRequestDto tagRequestDto;

    @BeforeEach
    void setUp() {
        tag = new Tag(1L, "requestTag");
        tagRequestDto = new TagRequestDto("requestTag");
    }

    @Test
    public void createTag() {

        when(modelMapper.toEntity(tagRequestDto)).thenReturn(tag);
        when(tagRepo.create(tag)).thenReturn(tag);

        AppResponseDto<Tag> responseDto = tagService.create(tagRequestDto);

        assertEquals(HttpStatus.CREATED.value(), responseDto.getHttpStatus());
        assertEquals("Tag successfully created", responseDto.getMessage());
        assertEquals(tag, responseDto.getData());
    }

    @Test
    public void getTag() {
        when(tagRepo.get(1L)).thenReturn(Optional.of(tag));
        when(tagRepo.get(100L)).thenThrow(ResourceNotFoundException.class);

        AppResponseDto<Tag> responseDto = tagService.get(1L);
        assertEquals(HttpStatus.OK.value(), responseDto.getHttpStatus());
        assertEquals(tag, responseDto.getData());
        assertThrows(ResourceNotFoundException.class, () -> tagService.get(100L));
    }

    @Test
    public void deleteTag() {
        when(tagRepo.delete(1L)).thenReturn(true);
        when(tagRepo.delete(100L)).thenReturn(false);

        AppResponseDto<Boolean> responseDto = tagService.delete(1L);
        assertEquals("tag deleted", responseDto.getMessage());
        assertTrue(responseDto.getData());
        assertThrows(ResourceNotFoundException.class, () -> tagService.delete(100L));
    }

    @Test
    public void getList() {
        when(tagRepo.getList()).thenReturn(Collections.singletonList(tag));
        AppResponseDto<List<Tag>> responseDto = tagService.getList();
        assertEquals("Tag list", responseDto.getMessage());
    }

    @Test
    public void saveTagListAndLinkTables() {

        Tag tag1 = new Tag(1L, "test1");
        Tag tag2 = new Tag(2L, "test2");

        TagRequestDto tagRequestDto1 = new TagRequestDto("test1");
        TagRequestDto tagRequestDto2 = new TagRequestDto("test2");

        List<TagRequestDto> requestDtoList = List.of(
               tagRequestDto1, tagRequestDto2);
        when(modelMapper.toEntity(tagRequestDto1)).thenReturn(tag1);
        when(modelMapper.toEntity(tagRequestDto2)).thenReturn(tag2);
        when(tagRepo.saveTagList(List.of(
                tag1,
                tag2), 1L))
                .thenReturn(List.of(tag1,tag2));

        List<Tag> tagList = tagService.saveTagListAndLinkTables(requestDtoList, 1L);

        assertEquals(tagList.size(), 2);
        assertEquals(tagList.get(0), tag1);
        assertEquals(tagList.get(1), tag2);
        assertNull(tagService.saveTagListAndLinkTables(null, 0L));
    }

}
