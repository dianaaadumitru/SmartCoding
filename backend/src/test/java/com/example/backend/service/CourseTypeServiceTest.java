package com.example.backend.service;

import com.example.backend.dto.CourseTypeDto;
import com.example.backend.entity.CourseType;
import com.example.backend.repository.CourseTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseTypeServiceTest {

    @Mock
    private CourseTypeRepository courseTypeRepository;

    @InjectMocks
    private CourseTypeService courseTypeService;

    @Test
    public void givenCourseTypeDto_addCourseType_returnEqualCourseTypeDto() {
        // given
        CourseTypeDto courseTypeDto = CourseTypeDto.builder()
                .type("test")
                .build();

        CourseType courseType = CourseType.builder()
                .type(courseTypeDto.getType())
                .build();

        when(courseTypeRepository.save(ArgumentMatchers.any(CourseType.class))).thenReturn(courseType);

        // when
        CourseTypeDto created = courseTypeService.addCourseType(courseTypeDto);

        // then
        assertEquals(created, courseTypeDto);
        verify(courseTypeRepository).save(ArgumentMatchers.any(CourseType.class));
    }

    @Test
    public void givenAllCourseTypes_getCourseTypes_returnAllCourseTypes() {
        // given
        List<CourseType> courseTypes = new ArrayList<>();
        courseTypes.add(CourseType.builder().type("test").build());

        when(courseTypeRepository.findAll()).thenReturn(courseTypes);

        // when
        List<CourseTypeDto> expected = courseTypeService.getAllCourseTypes();
        List<CourseTypeDto> courseTypeDtos = courseTypes.stream().map(courseType ->
                CourseTypeDto.builder()
                        .type(courseType.getType())
                        .build()).toList();

        // then
        assertEquals(expected, courseTypeDtos);
        verify(courseTypeRepository).findAll();
    }

    @Test
    public void givenCourseType_removeCourseType_deleteCourseTypeIfFound() {
        // given
        CourseType courseType = CourseType.builder()
                .courseTypeId(1L)
                .type("test")
                .build();

        when(courseTypeRepository.findById(courseType.getCourseTypeId())).thenReturn(Optional.of(courseType));

        // when
        courseTypeService.removeCourseType(courseType.getCourseTypeId());

        // then
        verify(courseTypeRepository).deleteById(courseType.getCourseTypeId());
    }

    @Test
    public void givenCourseType_removeCourseType_throwExceptionIfCourseTypeDoesntExist() {
        // given
        CourseType courseType = CourseType.builder()
                .courseTypeId(1L)
                .type("test")
                .build();

        when(courseTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> courseTypeService.removeCourseType(courseType.getCourseTypeId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenCourseTypeAndCourseTypeDto_updateCourseType_updateCourseTypeIfFound() {
        // given
        CourseType courseType = CourseType.builder()
                .courseTypeId(10L)
                .type("test")
                .build();

        CourseTypeDto newCourseTypeDto = CourseTypeDto.builder()
                .type("test update").build();

        when(courseTypeRepository.findById(courseType.getCourseTypeId())).thenReturn(Optional.of(courseType));

        // when
        courseTypeService.updateCourseType(courseType.getCourseTypeId(), newCourseTypeDto);

        // then
        assertEquals(newCourseTypeDto.getType(), courseType.getType());
        verify(courseTypeRepository).save(ArgumentMatchers.any(CourseType.class));
        verify(courseTypeRepository).findById(courseType.getCourseTypeId());
    }

    @Test
    public void givenCourseTypeAndCourseTypeDto_updateCourseType_throwExceptionIfCourseTypeDoesntExist() {
        // given
        CourseType courseType = CourseType.builder()
                .courseTypeId(10L)
                .type("test")
                .build();

        CourseTypeDto newCourseTypeDto = CourseTypeDto.builder()
                .type("new type").build();

        when(courseTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> courseTypeService.updateCourseType(courseType.getCourseTypeId(), newCourseTypeDto);

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenCourseType_findCourseTypeById_returnEqualCourseTypeIfFound() {
        // given
        CourseType courseType = CourseType.builder()
                .courseTypeId(10L)
                .type("test")
                .build();

        when(courseTypeRepository.findById(courseType.getCourseTypeId())).thenReturn(Optional.of(courseType));

        // when
        CourseTypeDto expectedDto = courseTypeService.getCourseTypeById(courseType.getCourseTypeId());
        CourseTypeDto actualDto = CourseTypeDto.builder()
                .type(courseType.getType())
                .id(courseType.getCourseTypeId())
                .build();

        // then
        assertEquals(actualDto, expectedDto);
        verify(courseTypeRepository).findById(courseType.getCourseTypeId());
    }

    @Test
    public void givenCourseType_findCourseTypeById_throwExceptionIfCourseTypeDoesntExist() {
        // given
        CourseType courseType = CourseType.builder()
                .courseTypeId(10L)
                .type("test")
                .build();

        when(courseTypeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        Executable call = () -> courseTypeService.getCourseTypeById(courseType.getCourseTypeId());

        // then
        assertThrows(RuntimeException.class, call);
    }

    @Test
    public void givenCourseType_findCourseTypeByType_returnEqualCourseTypeIfFound() {
        // given
        CourseType courseType = CourseType.builder()
                .courseTypeId(10L)
                .type("test")
                .build();

        when(courseTypeRepository.findByType(courseType.getType())).thenReturn(Optional.of(courseType));

        // when
        CourseTypeDto expectedDto = courseTypeService.findByCourseType(courseType.getType());
        CourseTypeDto actualDto = CourseTypeDto.builder()
                .type(courseType.getType())
                .id(courseType.getCourseTypeId())
                .build();

        // then
        assertEquals(actualDto, expectedDto);
        verify(courseTypeRepository).findByType(courseType.getType());
    }

    @Test
    public void givenCourseType_findCourseTypeByType_throwExceptionIfCourseTypeDoesntExist() {
        // given
        CourseType courseType = CourseType.builder()
                .courseTypeId(10L)
                .type("test")
                .build();

        when(courseTypeRepository.findByType(anyString())).thenReturn(Optional.empty());

        // when
        Executable call = () -> courseTypeService.findByCourseType(courseType.getType());

        // then
        assertThrows(RuntimeException.class, call);
    }

}