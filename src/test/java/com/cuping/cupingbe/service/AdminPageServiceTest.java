package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import com.cuping.cupingbe.dto.AdminPageResponseDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.CafeRepository;
import com.cuping.cupingbe.s3.S3Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminPageServiceTest {
    @Mock
    private BeanRepository beanRepository;
    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private UtilService utilService;
    @Mock
    private S3Uploader s3Uploader;
    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private AdminPageRequestDto adminPageRequestDto;
    @InjectMocks
    private AdminPageService adminPageService;
    private User user;
    private Cafe cafe;
    private Bean bean;
    @BeforeEach
    @DisplayName("초기세팅")
    public void setup() {
        MultipartFile mockFile = new MockMultipartFile("testFile", new byte[0]);
        adminPageRequestDto = new AdminPageRequestDto(mockFile, mockFile,"TestName", "TestSummary",
                "TestInfo", "2", "TestFlavor",
                true, true, true, true);

        bean = new Bean(1L, "TestName", "TestOriginName",
                "TestImageURL","TestGraph" ,"TestSummary", "단맛/신맛/쓴맛",
                "TestInfo", "2", "TestOrigin", 0, true, true, true, true);

        cafe = new Cafe(1L, user, bean, "Test Address", "TestNumber", "TestName",
                "TestX", "TestY", false,
                "TestBusinessImageURL", "TestCafeImageURL", "서울시", "관악구", "TestDetailLink",
                "TestHomePageLink", "TestOpenDay", "TestOpenTime");

        user = new User(1L,"TestId", "TestNickName", "TestPassWord",
                UserRoleEnum.ADMIN, null, "TestEmail", null);

        userDetails = new UserDetailsImpl(user,user.getUserId());

    }
    @Test
    @DisplayName("원두 등록")
    public void createBean() throws IOException {
        //given
        String imgUrl = "TestUrl";
        doNothing().when(utilService).checkRoleAdmin(user.getRole());
        given(utilService.checkBean(adminPageRequestDto.getOrigin() + adminPageRequestDto.getBeanName()
                , adminPageRequestDto.getRoastingLevel(), true)).willReturn(bean);
        given(s3Uploader.upload(adminPageRequestDto.getImage())).willReturn(imgUrl);
        given(s3Uploader.upload(adminPageRequestDto.getBeanGraph())).willReturn(imgUrl);
        when(beanRepository.save(any(Bean.class))).thenReturn(bean);
        //when
        ResponseEntity<Message> response = adminPageService.createBean(adminPageRequestDto,userDetails);
        //then
        verify(utilService,times(1)).checkRoleAdmin(user.getRole());
        verify(utilService,times(1)).checkBean(adminPageRequestDto.getOrigin() + adminPageRequestDto.getBeanName()
                , adminPageRequestDto.getRoastingLevel(), true);
        verify(s3Uploader,times(2)).upload(adminPageRequestDto.getImage());
        verify(s3Uploader,times(2)).upload(adminPageRequestDto.getBeanGraph());
        verify(beanRepository,times(1)).save(any(Bean.class));
        assertThat(response.getBody().getData()).isEqualTo(null);
        assertThat(response.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    @DisplayName("승인되지 않은 카페 조회")
    public void getPermitCafe() {
        // given
        doNothing().when(utilService).checkRoleAdmin(user.getRole());
        List<Cafe> cafeList = new ArrayList<>();
        cafeList.add(cafe);
        List<AdminPageResponseDto> adminPageResponseDtoList = new ArrayList<>();
        when(cafeRepository.findAllByPermit(false)).thenReturn(cafeList);
        List<Cafe> cafeList1 = cafeRepository.findAllByPermit(false);
        for (Cafe cafe : cafeList1) {
            adminPageResponseDtoList.add(new AdminPageResponseDto(cafe));
        }
        //when
        ResponseEntity<Message> response = adminPageService.getPermitCafe(userDetails);
        //then
        verify(utilService,times(1)).checkRoleAdmin(user.getRole());
        verify(cafeRepository,times(2)).findAllByPermit(false);
        assertThat(response.getBody().getData().equals(adminPageResponseDtoList));
        assertThat(response.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    @DisplayName("카페 승인")
    public void permitCafe() {
        //given
        doNothing().when(utilService).checkRoleAdmin(user.getRole());
        when(utilService.checkCafeId(cafe.getId())).thenReturn(cafe);
        when(cafeRepository.save(cafe.setPermit(true))).thenReturn(cafe);
        //when
        ResponseEntity<Message> response = adminPageService.permitCafe(1L, userDetails);
        //then
        verify(utilService,times(1)).checkRoleAdmin(user.getRole());
        verify(utilService,times(1)).checkCafeId(cafe.getId());
        verify(cafeRepository,times(1)).save(cafe.setPermit(true));
        assertThat(response.getBody().getData()).isEqualTo(null);
        assertThat(response.getStatusCode().equals(HttpStatus.OK));
    }
    @Test
    @DisplayName("원두 삭제")
    public void deleteBean() {
        //given
        doNothing().when(utilService).checkRoleAdmin(user.getRole());
        given(utilService.checkBean(bean.getId())).willReturn(bean);
        doNothing().when(beanRepository).delete(bean);
        //when
        ResponseEntity<Message> response = adminPageService.deleteBean(bean.getId(),userDetails);
        //then
        verify(utilService,times(1)).checkRoleAdmin(user.getRole());
        verify(utilService,times(1)).checkBean(bean.getId());
        verify(beanRepository,times(1)).delete(bean);
        assertThat(Objects.requireNonNull(response.getBody()).getData()).isEqualTo(null);
        assertThat(response.getStatusCode().equals(HttpStatus.OK));
    }
    @Test
    @DisplayName("원두 전체 조회")
    public void findAllBean() {
        //given
        doNothing().when(utilService).checkRoleAdmin(user.getRole());
        List<Bean> beanList = new ArrayList<>();
        beanList.add(bean);
        given(beanRepository.findAll()).willReturn(beanList);
        //when
        ResponseEntity<Message> response = adminPageService.findAllBean(userDetails.getUser());
        //then
        verify(utilService,times(1)).checkRoleAdmin(user.getRole());
        verify(beanRepository,times(1)).findAll();
        assertThat(response.getStatusCode().equals(HttpStatus.OK));
        assertThat(response.getBody().getData().equals(beanList));
    }
}
