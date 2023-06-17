package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.BeanByCafeRequestDto;
import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.cuping.cupingbe.dto.OwnerResponseDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.security.UserDetailsImpl;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.CafeRepository;
import com.cuping.cupingbe.s3.S3Uploader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerPageServiceTest {
    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private UtilService utilService;
    @Mock
    private S3Uploader s3Uploader;
    @Mock
    private UserDetailsImpl userDetails;
    @Mock
    private OwnerPageService ownerService;
    @InjectMocks
    private OwnerPageService ownerPageService;
    private OwnerPageRequestDto ownerPageRequestDto;
    private BeanByCafeRequestDto beanByCafeRequestDto;
    private User user;
    private Cafe cafe;
    private Bean bean;
    @BeforeEach
    @DisplayName("초기세팅")
    public void setup() {
        MultipartFile mockFile = new MockMultipartFile("testFile", new byte[0]);
        ownerPageRequestDto = new OwnerPageRequestDto("TestStoreName", "Test Adress",
                "TestStoreNumber", mockFile, mockFile);

        bean = new Bean(1L, "TestName", "TestOriginName",
                "TestImageURL", "TestSummary", "단맛/신맛/쓴맛",
                "TestInfo", "2", "TestOrigin", 0, true, true, true, true);

        cafe = new Cafe(1L, user, bean, "Test Address", "TestNumber","TestName",
                "TestX", "TestY", false,
                "TestbusinessImageURL", "TestCafeImageURL", "서울시", "관악구");

        user = new User(1L,"TestId", "TestNickName", "TestPassWord",
                UserRoleEnum.OWNER, null, "TestEmail", null);

        userDetails = new UserDetailsImpl(user,user.getUserId());

        beanByCafeRequestDto = new BeanByCafeRequestDto(cafe.getCafeAddress(),
                bean.getBeanName(),bean.getRoastingLevel(),bean.getOrigin());

    }
    @Test
    @DisplayName("카페 등록 요청")
    public void createCafe() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cafeNode = mapper.createObjectNode();
        cafeNode.put("address_name", "Test address");
        cafeNode.put("category_group_code", "CE7");
        cafeNode.put("category_group_name", "카페");
        cafeNode.put("category_name", "음식점 > 카페");
        cafeNode.put("distance", "");
        cafeNode.put("id", "1");
        cafeNode.put("phone", "TestNumber");
        cafeNode.put("place_name", "TestName");
        cafeNode.put("place_url", "TestPlace_URL");
        cafeNode.put("road_address_name", "Test Address");
        cafeNode.put("x", "TestX");
        cafeNode.put("y", "TestY");
        ArrayNode documentsArrayNode = mapper.createArrayNode();
        documentsArrayNode.add(cafeNode);
        JsonNode jsonNode = documentsArrayNode;
        when(ownerService.setCreateCafe(ownerPageRequestDto)).thenReturn(jsonNode);
        when(s3Uploader.upload(ownerPageRequestDto.getAuthImage())).thenReturn("TestBusinessImageURL");
        when(s3Uploader.upload(ownerPageRequestDto.getCafeImage())).thenReturn("TestCafeImageURL");
        when(cafeRepository.save(any(Cafe.class))).thenReturn(cafe);
        //when
        ownerService.checkCreateCafe(user, ownerPageRequestDto.getStoreAddress());
        JsonNode documents = ownerService.setCreateCafe(ownerPageRequestDto);
        String businessImage = s3Uploader.upload(ownerPageRequestDto.getAuthImage());
        String cafeImage;
        if (ownerPageRequestDto.getCafeImage() != null) {
            cafeImage = s3Uploader.upload(ownerPageRequestDto.getCafeImage());
        } else { cafeImage = "";}
        Cafe tmpCafe = cafeRepository.save(new Cafe(user, ownerPageRequestDto, documents, businessImage, cafeImage));
        ResponseEntity<Message> responseEntity = new ResponseEntity<>
                (new Message("가게 등록 성공", null), HttpStatus.OK);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cafe.getId().equals(tmpCafe.getId()));
        assertThat(responseEntity.getBody().getMessage().equals("가게 등록 성공"));
    }
    @Test
    @DisplayName("카페 삭제 요청")
    public void deleteCafeTest() {
        // given
        when(utilService.checkUserId(user.getUserId())).thenReturn(user);
        when(utilService.checkCafeId(cafe.getId())).thenReturn(cafe);
        when(cafeRepository.findByOwnerIdAndId(user.getId(), cafe.getId())).thenReturn(Optional.of(cafe));
        doNothing().when(s3Uploader).delete(cafe.getCafeImage());
        doNothing().when(s3Uploader).delete(cafe.getBusinessImage());
        doNothing().when(cafeRepository).deleteById(cafe.getId());

        // when
        ResponseEntity<Message> responseEntity = ownerPageService.deleteCafe(cafe.getId(), user);

        // then
        verify(utilService,times(2)).checkCafeId(cafe.getId());
        verify(utilService,times(2)).checkUserId(user.getUserId());
        verify(s3Uploader,times(1)).delete(cafe.getCafeImage());
        verify(s3Uploader,times(1)).delete(cafe.getBusinessImage());
        verify(cafeRepository,times(1)).deleteById(cafe.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getData()).isNull();
    }

    @Test
    @DisplayName("카페 조회")
    public void getCafe() {
        // given
        List<Cafe> cafeList = new ArrayList<>();
        cafeList.add(cafe);
        when(cafeRepository.findAllByOwnerId(user.getId())).thenReturn(cafeList);
        // when
        ResponseEntity<Message> responseEntity = ownerPageService.getCafe(user);
        // then
        verify(cafeRepository,times(1)).findAllByOwnerId(user.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((Map<String, OwnerResponseDto>) responseEntity.getBody().getData())
                .get(cafe.getCafeAddress()).getCafeName())
                .isEqualTo(cafe.getCafeName());
    }
    @Test
    @DisplayName("카페에 원두 등록")
    public void addBeanByCafe() {
        // given
        when(utilService.checkBean(beanByCafeRequestDto.getBeanOrigin() + beanByCafeRequestDto.getBeanName(), beanByCafeRequestDto.getBeanRoastingLevel(), false)).thenReturn(bean);
        when(cafeRepository.findFirstByCafeAddressAndOwnerId(cafe.getCafeAddress(), user.getId())).thenReturn(Optional.of(cafe));
        when(cafeRepository.save(any(Cafe.class))).thenReturn(cafe);
        // when
        ResponseEntity<Message> response = ownerPageService.addBeanByCafe(userDetails.getUser(), beanByCafeRequestDto);
        // then
        verify(utilService,times(1)).checkBean(beanByCafeRequestDto.getBeanOrigin() + beanByCafeRequestDto.getBeanName(), beanByCafeRequestDto.getBeanRoastingLevel(), false);
        verify(cafeRepository,times(1)).findFirstByCafeAddressAndOwnerId(cafe.getCafeAddress(), user.getId());
        verify(cafeRepository,times(1)).save(any(Cafe.class));
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    @DisplayName("카페에 등록된 원두 삭제")
    public void deleteBeanBycafe() {
        //given
        when(utilService.checkBean(beanByCafeRequestDto.getBeanOrigin() + beanByCafeRequestDto.getBeanName(), beanByCafeRequestDto.getBeanRoastingLevel(), false)).thenReturn(bean);
        when(cafeRepository.findByCafeAddressAndBeanIdAndOwnerId(beanByCafeRequestDto.getCafeAddress(),bean.getId(),user.getId())).thenReturn(Optional.of(cafe));
        doNothing().when(cafeRepository).delete(any(Cafe.class));
        //when
        ResponseEntity<Message> response = ownerPageService.deleteBeanByCafe(beanByCafeRequestDto, userDetails.getUser());
        //then
        verify(utilService,times(1)).checkBean(beanByCafeRequestDto.getBeanOrigin() + beanByCafeRequestDto.getBeanName(), beanByCafeRequestDto.getBeanRoastingLevel(), false);
        verify(cafeRepository,times(1)).findByCafeAddressAndBeanIdAndOwnerId(beanByCafeRequestDto.getCafeAddress(),bean.getId(),user.getId());
        verify(cafeRepository,times(1)).delete(any(Cafe.class));
        assertThat(response.getBody().getData()).isEqualTo(null);
        assertThat(response.getHeaders().equals(HttpStatus.OK));
    }
}