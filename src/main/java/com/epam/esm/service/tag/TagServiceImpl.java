package com.epam.esm.service.tag;

import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.exception.DataPersistenceException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.mapper.tag.TagModelMapper;
import com.epam.esm.repository.tag.TagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepo tagRepo;
    private final TagModelMapper modelMapper;

    @Override
    public AppResponseDto<Tag> create(TagRequestDto requestDto) {
        try {
            return new AppResponseDto<>(HttpStatus.CREATED.value(), "Tag successfully created", tagRepo.create(modelMapper.toEntity(requestDto)));
        } catch (DataAccessException e) {
            throw new DataPersistenceException("Tag creation error");
        }
    }

    @Override
    public AppResponseDto<Tag> get(Long id) {
        Optional<Tag> tag = tagRepo.get(id);
        if (tag.isPresent()) {
            return new AppResponseDto<>(HttpStatus.OK.value(), "tag", tag.get());
        }
        throw new ResourceNotFoundException(id);
    }

    @Override
    public AppResponseDto<Boolean> delete(Long id) {
        if (tagRepo.delete(id)) {
            return new AppResponseDto<>(HttpStatus.OK.value(), "tag deleted", true);
        }
        throw new ResourceNotFoundException(id);
    }

    @Override
    public AppResponseDto<List<Tag>> getList() {
        return new AppResponseDto<>(HttpStatus.OK.value(), "Tag list", tagRepo.getList());
    }

    @Override
    public List<Tag> saveTagListAndLinkTables(List<TagRequestDto> requestDtoList, long giftCertificateId) {
        if(requestDtoList == null || requestDtoList.size() == 0){
            return null;
        }
        return tagRepo.saveTagList(requestDtoList
                        .stream()
                        .map(modelMapper::toEntity)
                        .collect(Collectors.toList()),
                giftCertificateId);
    }

    @Override
    public List<Tag> getTagListByGiftId(long giftId) {
         return tagRepo.getByGiftId(giftId);
    }
}
