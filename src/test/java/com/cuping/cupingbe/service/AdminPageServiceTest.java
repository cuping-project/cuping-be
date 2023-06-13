package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.AdminPageRequestDto;
import com.cuping.cupingbe.dto.AdminPageResponseDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.exception.CustomException;
import com.cuping.cupingbe.global.exception.ErrorCode;
import com.cuping.cupingbe.repository.BeanRepository;
import com.cuping.cupingbe.repository.CafeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    @Mock
    private Bean bean;
    @Mock
    private AdminPageRequestDto adminPageRequestDto;
    @BeforeEach
    @DisplayName("초기세팅")
    public void setup() {
        MultipartFile mockFile = new MockMultipartFile("testFile", new byte[0]);
        adminPageRequestDto = new AdminPageRequestDto(mockFile, "TestName", "TestSummary",
                      "TestInfo", "2", "TestFlavor",
                        "TestOrigin", "쓴맛/단맛/신맛");

        bean = new Bean(1L, "TestName", "TestOriginName",
                "TestImageURL", "TestSummary", "단맛/신맛/쓴맛",
                "TestInfo", "2", "TestFlavor", "TestOrigin", 0);

        cafe = new Cafe(1L, user, bean, "TestAddress", "TestNumber","TestName",
                "TestX", "TestY", false,
                "TestbusinessImageURL", "TestCafeImageURL");

        user = new User(1L,"TestId", "TestNickName", "TestPassWord",
                UserRoleEnum.ADMIN, null, "TestEmail", null);
    }
    @Test
    @DisplayName("원두 등록")
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
    @DisplayName("승인되지 않은 카페 조회")
    public void getPermitCafe() {
        // given
        List<Cafe> cafeList = new ArrayList<>();
        List<AdminPageResponseDto> adminPageResponseDtoList = new ArrayList<>();
        // when
        checkAdmin(user);
        cafeList.add(cafe);
        when(cafeRepository.findAllByPermit(false)).thenReturn(cafeList);
        // then
        List<Cafe> cafeList1 = cafeRepository.findAllByPermit(false);
        for (Cafe cafe : cafeList1) {
            adminPageResponseDtoList.add(new AdminPageResponseDto(cafe));
        }
        assertThat(adminPageResponseDtoList.get(0).getCafeId()).isEqualTo(cafeList.get(0).getId());
    }
    @Test
    @DisplayName("카페 승인")
    public void permitCafe() {
        //given
        String imgUrl = "TestURL";
        Bean bean = new Bean(imgUrl, adminPageRequestDto);
        Cafe cafe2 = new Cafe(user, cafe, bean);
        //when
        checkAdmin(user);
        when(utilService.checkCafeId(cafe.getId())).thenReturn(cafe2);
        when(cafeRepository.save(cafe2)).thenReturn(cafe2);
        //then
        cafe2 = utilService.checkCafeId(cafe.getId());
        cafe2.setPermit(true);
        Cafe testCafe = cafeRepository.save(cafe2);
        assertThat(testCafe.getPermit());
    }
    @Test
    @DisplayName("원두 삭제")
    public void deleteBean() {
        //given
        String imgUrl = "TestURL";
        Bean bean = new Bean(imgUrl, adminPageRequestDto);
        when(utilService.checkBean(bean.getId())).thenReturn(bean);
        //when
        checkAdmin(user);
        Bean testBean = utilService.checkBean(bean.getId());
        beanRepository.delete(testBean);
        Optional<Bean> resultBean = beanRepository.findById(testBean.getId());
        //then
        assertThat(resultBean.isEmpty());
    }
    @Test
    @DisplayName("원두 전체 조회")
    public void findAllBean() {
        //given
        String imgUrl = "TestURL";
        Bean bean = new Bean(imgUrl, adminPageRequestDto);
        List<Bean> beanList = new ArrayList<>();
        beanList.add(bean);
        when(beanRepository.findAll()).thenReturn(beanList);
        //when
        List<Bean> testList = beanRepository.findAll();
        //then
        assertThat(testList.size() > 0);
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