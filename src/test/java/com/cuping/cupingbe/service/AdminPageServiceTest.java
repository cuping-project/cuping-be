package com.cuping.cupingbe.service;


import com.cuping.cupingbe.dto.AdminPageRequestDto;
import com.cuping.cupingbe.dto.AdminPageResponseDto;
import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AdminPageServiceTest {

    @Mock
    private BeanRepository beanRepository;
    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private User user;
    @Mock
    private UtilService utilService;
    @Mock
    private Cafe cafe;
    private AdminPageRequestDto adminPageRequestDto;




    @BeforeEach
    @DisplayName("초기세팅")
    public void setup() {
        adminPageRequestDto = new AdminPageRequestDto();
        MultipartFile mockFile = new MockMultipartFile("testFile", new byte[0]);
        adminPageRequestDto.setImage(mockFile);
        adminPageRequestDto.setBeanName("TestName");
        adminPageRequestDto.setBeanSummary("TestSummary");
        adminPageRequestDto.setBeanInfo("TestInfo");
        adminPageRequestDto.setRoastingLevel("2");
        adminPageRequestDto.setFlavor("TestFlavor");
        adminPageRequestDto.setOrigin("TestOrigin");
        adminPageRequestDto.setHashTag("쓴맛/단맛");

        cafe = new Cafe();
        cafe.setId(1L);
        cafe.setCafeName("TestName");
        cafe.setCafeAddress("TestAddress");
        cafe.setBean(null);
        cafe.setOwner(user);
        cafe.setCafePhoneNumber("TestNumber");
        cafe.setBusinessImage("TestURL");
        cafe.setCafeImage("TestURL");
        cafe.setX("TestX");
        cafe.setY("TestY");
        cafe.setPermit(false);


        user = new User();
        user.setId(1L);
        user.setUserId("TestId");
        user.setEmail("TestEmail");
        user.setPassword("TestPassWord");
        user.setNickname("TestNickName");
        user.setRole(UserRoleEnum.ADMIN);
    }

    @Test
    @DisplayName("원두 생성 성공")
    public void createBean() {
        //given
        String imgUrl = "TestURL";
        Bean bean = new Bean(imgUrl, adminPageRequestDto);

        //when
        checkCreateBean(user, adminPageRequestDto);
        when(beanRepository.save(bean)).thenReturn(bean);
        //then
        Bean saveBean = beanRepository.save(bean);
        assertThat(saveBean).isEqualTo(bean);
    }

    @Test
    @DisplayName("카페 조회")
    public void getPermitCafe() {
        // given
        List<Cafe> cafeList = new ArrayList<>();
        List<AdminPageResponseDto> adminPageResponseDtoList = new ArrayList<>();
        List<AdminPageResponseDto> adminPageResponseDtoList1 = new ArrayList<>();
        // when
        checkAdmin(user);
        cafeList.add(cafe);
        when(cafeRepository.findAllByPermit(false)).thenReturn(cafeList);
        adminPageResponseDtoList1.add(new AdminPageResponseDto(cafe));

        // then
        List<Cafe> cafeList1 = cafeRepository.findAllByPermit(false);
        for (Cafe cafe : cafeList1) {
            adminPageResponseDtoList.add(new AdminPageResponseDto(cafe));
        }
        assertThat(adminPageResponseDtoList.get(0).getCafeId()).isEqualTo(adminPageResponseDtoList1.get(0).getCafeId());
    }




    public void checkCreateBean(User user, AdminPageRequestDto adminPageRequestDto) {
        checkAdmin(user);
        utilService.checkBean(adminPageRequestDto.getOrigin() + adminPageRequestDto.getBeanName()
                , adminPageRequestDto.getRoastingLevel(), true);
    }
    public void checkAdmin(User user) {
        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ADMIN);
        }
    }


}