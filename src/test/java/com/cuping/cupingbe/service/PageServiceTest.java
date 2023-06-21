package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.DetailPageResponseDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.Comment;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.CafeRepository;
import com.cuping.cupingbe.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageServiceTest {

    @Mock
    private BeanRepository beanRepository;
    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UtilService utilService;
    @InjectMocks
    private PageService pageService;

    @Test
    @DisplayName("getSearchPage keyword = \"\"")
    void getSearchPageNull() {
        // given
        Bean bean = new Bean();
        Bean bean2 = new Bean();
        when(beanRepository.findAll()).thenReturn(List.of(bean, bean2));

        // when
        ResponseEntity<Message> response = pageService.getSearchPage("");

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isEqualTo("Success");
        assertThat(Objects.requireNonNull(response.getBody()).getData()).isEqualTo(List.of(bean, bean2));

        verify(beanRepository).findAll();
    }

    @Test
    @DisplayName("getSearchPage keyword = Test")
    void getSearchPageTest() {
        // given
        Bean bean = new Bean();
        when(beanRepository.findByBeanOriginNameContaining("Test")).thenReturn(List.of(bean));

        // when
        ResponseEntity<Message> response2 = pageService.getSearchPage("Test");

        // then
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response2.getBody()).getData()).isEqualTo(List.of(bean));

        verify(beanRepository).findByBeanOriginNameContaining("Test");
    }

    @Test
    @DisplayName("getDetailPage address = null")
    void getDetailPageNull() {
        // given
        Long beanId = 1L;
        Bean bean = new Bean();
        Cafe cafe = new Cafe();
        Cafe cafe2 = new Cafe();
        Comment comment = new Comment();
        List<Cafe> cafeList = new ArrayList<>();
        cafeList.add(cafe);
        cafeList.add(cafe2);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);

        when(cafeRepository.findByBeanAndCafeAddressContaining(bean, null)).thenReturn(cafeList);
        when(commentRepository.findByBean(bean,1)).thenReturn(commentList);
        when(commentRepository.findByBean_id(bean.getId())).thenReturn(commentList);
        when(utilService.checkBean(beanId)).thenReturn(bean);

        // when
        ResponseEntity<Message> response = pageService.getDetailPage(beanId, null, 1);
        DetailPageResponseDto responseDto = (DetailPageResponseDto) Objects.requireNonNull(response.getBody()).getData();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isEqualTo("Success");
        assertThat(responseDto.getBean()).isEqualTo(bean);
        assertThat(responseDto.getCafeList()).isEqualTo(cafeList);
        assertThat(responseDto.getCommentList()).isEqualTo(commentList);
    }

    @Test
    @DisplayName("getDetailPage address = 강남구")
    void getDetailPageAddress() {
        // given
        Long beanId = 1L;
        Bean bean = new Bean();
        Cafe cafe = new Cafe();
        Comment comment = new Comment();
        List<Cafe> cafeList = new ArrayList<>();
        cafeList.add(cafe);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);

        when(cafeRepository.findByBeanAndCafeAddressContaining(bean, "강남구")).thenReturn(cafeList);
        when(commentRepository.findByBean(bean,1)).thenReturn(commentList);
<<<<<<< HEAD
        when(commentRepository.findByBean(bean)).thenReturn(commentList);
=======
        when(commentRepository.findByBean_id(bean.getId())).thenReturn(commentList);
>>>>>>> 39cd7c19ea5911477304408f3912c4e53cc9627f
        when(utilService.checkBean(beanId)).thenReturn(bean);

        // when
        ResponseEntity<Message> response = pageService.getDetailPage(beanId, "강남구", 1);
        DetailPageResponseDto responseDto = (DetailPageResponseDto) Objects.requireNonNull(response.getBody()).getData();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isEqualTo("Success");
        assertThat(responseDto.getBean()).isEqualTo(bean);
        assertThat(responseDto.getCafeList()).isEqualTo(cafeList);
        assertThat(responseDto.getCommentList()).isEqualTo(commentList);
    }

    @Test
    @DisplayName("setDetailPage address = null")
    void setDetailPageCafeNull() {
        // given
        Bean bean = new Bean();
        Cafe cafe = new Cafe();
        Cafe cafe2 = new Cafe();
        List<Cafe> cafeList = new ArrayList<>();
        cafeList.add(cafe);
        cafeList.add(cafe2);

        when(cafeRepository.findByBeanAndCafeAddressContaining(bean, "null"))
                .thenReturn(cafeList);

        // when
        List<Cafe> ResponseCafeList = pageService.setDetailPageCafe(bean, "null");

        // then
        assertThat(ResponseCafeList).isEqualTo(cafeList);
    }

    @Test
    @DisplayName("setDetailPage address = 강남구")
    void setDetailPageCafeAddress() {
        // given
        Bean bean = new Bean();
        Cafe cafe = new Cafe();
        List<Cafe> cafeList = new ArrayList<>();
        cafeList.add(cafe);

        when(cafeRepository.findByBeanAndCafeAddressContaining(bean, "강남구"))
                .thenReturn(cafeList);

        // when
        List<Cafe> ResponseCafeList = pageService.setDetailPageCafe(bean, "강남구");

        // then
        assertThat(ResponseCafeList).isEqualTo(cafeList);
    }

    @Test
    @DisplayName("setDetailPageComment commentList = null")
    void setDetailPageCommentNull() {
        // given
        Bean bean = new Bean();

        when(commentRepository.findByBean(bean,1)).thenReturn(null);

        // when
        List<Comment> responseCommentList2 = pageService.setDetailPageComment(bean, 1);

        // then
        assertThat(responseCommentList2).isEqualTo(null);
    }

    @Test
    @DisplayName("setDetailPageComment commentList = isPresent")
    void setDetailPageCommentIsPresent() {
        // given
        Bean bean = new Bean();
        Comment comment = new Comment();
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);

        when(commentRepository.findByBean(bean,1)).thenReturn(commentList);

        // when
        List<Comment> responseCommentList = pageService.setDetailPageComment(bean, 1);

        // then
        assertThat(responseCommentList).isEqualTo(commentList);
    }
}
