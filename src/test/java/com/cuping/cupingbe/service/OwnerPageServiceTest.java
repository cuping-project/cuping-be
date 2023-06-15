package com.cuping.cupingbe.service;


import com.cuping.cupingbe.dto.BeanByCafeRequestDto;
import com.cuping.cupingbe.dto.OwnerPageRequestDto;
import com.cuping.cupingbe.dto.OwnerResponseDto;
import com.cuping.cupingbe.entity.Bean;
import com.cuping.cupingbe.entity.Cafe;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.BeanRepository;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnerPageServiceTest {
    @Mock
    private OwnerPageService ownerPageService;
    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private S3Uploader s3Uploader;

    private User user;
    private Bean bean;
    private Cafe cafe;
    private OwnerPageRequestDto ownerPageRequestDto;
    private OwnerResponseDto ownerResponseDto;
    private BeanByCafeRequestDto beanByCafeRequestDto;
    @BeforeEach
    @DisplayName("초기세팅")
    public void setup() {
        MultipartFile mockFile = new MockMultipartFile("testFile", new byte[0]);
        ownerPageRequestDto = new OwnerPageRequestDto("TestStoreName", "TestAdress",
                "TestStoreNumber", mockFile, mockFile);

        bean = new Bean(1L, "TestName", "TestOriginName",
                "TestImageURL", "TestSummary", "단맛/신맛/쓴맛",
                "TestInfo", "2", "TestFlavor", "TestOrigin", 0);

        user = new User(1L,"TestId", "TestNickName", "TestPassWord",
                UserRoleEnum.OWNER, null, "TestEmail", null);

        cafe = new Cafe(1L, user, bean, "TestAddress", "TestNumber","TestName",
                "TestX", "TestY", false,
                "TestBusinessImageURL", "TestCafeImageURL");

        ownerResponseDto = new OwnerResponseDto(cafe);

        beanByCafeRequestDto = new BeanByCafeRequestDto(cafe.getCafeAddress(),
                bean.getBeanName(),bean.getRoastingLevel(),bean.getOrigin());
    }
    @Test
    @DisplayName("카페 등록 요청")
    public void createCafe() throws Exception {
        //given
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cafeNode = mapper.createObjectNode();
        cafeNode.put("address_name", "Testaddress");
        cafeNode.put("category_group_code", "CE7");
        cafeNode.put("category_group_name", "카페");
        cafeNode.put("category_name", "음식점 > 카페");
        cafeNode.put("distance", "");
        cafeNode.put("id", "1");
        cafeNode.put("phone", "TestNumber");
        cafeNode.put("place_name", "TestName");
        cafeNode.put("place_url", "TestPlace_URL");
        cafeNode.put("road_address_name", "TestAddress");
        cafeNode.put("x", "TestX");
        cafeNode.put("y", "TestY");
        ArrayNode documentsArrayNode = mapper.createArrayNode();
        documentsArrayNode.add(cafeNode);
        JsonNode jsonNode = documentsArrayNode;
        when(ownerPageService.setCreateCafe(ownerPageRequestDto)).thenReturn(jsonNode);
        when(s3Uploader.upload(ownerPageRequestDto.getAuthImage())).thenReturn("TestBusinessImageURL");
        when(s3Uploader.upload(ownerPageRequestDto.getAuthImage())).thenReturn("TestCafeImageURL");
        when(cafeRepository.save(any(Cafe.class))).thenReturn(cafe);
        //when
        ownerPageService.checkCreateCafe(user, ownerPageRequestDto.getStoreAddress());
        JsonNode documents = ownerPageService.setCreateCafe(ownerPageRequestDto);
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
    @DisplayName("카페 삭제")
    public void deleteCafe () {
        //given
        when(ownerPageService.checkDeleteCafe(cafe.getId(), user)).thenReturn(cafe);
        //when
        s3Uploader.delete(ownerPageService.checkDeleteCafe(cafe.getId(),user).getBusinessImage());
        s3Uploader.delete(ownerPageService.checkDeleteCafe(cafe.getId(),user).getCafeImage());
        cafeRepository.delete(ownerPageService.checkDeleteCafe(cafe.getId(),user));
        Optional<Cafe> resultCafe = cafeRepository.findById(cafe.getId());
        //then
        assertThat(resultCafe.isEmpty());
    }
    @Test
    @DisplayName("카페 조회")
    public void getCafe() {
        // given
        Map<String, OwnerResponseDto> cafeMap = new HashMap<>();
        cafeMap.put(cafe.getCafeAddress(), ownerResponseDto);
        when(ownerPageService.createCafeMap(user.getId())).thenReturn(cafeMap);

        // when
        ownerPageService.checkRoleOwner(user.getRole());
        ResponseEntity<Message> responseEntity = new ResponseEntity<>(
                new Message("사장 카페 조회.", ownerPageService.createCafeMap(user.getId())), HttpStatus.OK);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(((Map<String, OwnerResponseDto>) responseEntity.getBody().getData())
                .get(cafe.getCafeAddress()).getCafeName())
                .isEqualTo(cafe.getCafeName());
    }
    @Test
    @DisplayName("카페에 원두 등록")
    public void addBeanByCafe() {
        //given
        when(ownerPageService.checkBeanByCafe(user, beanByCafeRequestDto, "add")).thenReturn(cafe);
        when(cafeRepository.save(cafe)).thenReturn(cafe);
        //when
        Cafe testCafe = cafeRepository.save(ownerPageService.checkBeanByCafe(user, beanByCafeRequestDto, "add"));
        //then
        assertThat(testCafe.equals(cafe));
    }
    @Test
    @DisplayName("카페에 등록된 원두 삭제")
    public void deleteBeanBycafe() {
        //given
        when(ownerPageService.checkBeanByCafe(user, beanByCafeRequestDto, "delete")).thenReturn(cafe);
        //when
        Cafe resultCafe = ownerPageService.checkBeanByCafe(user, beanByCafeRequestDto, "delete");
        cafeRepository.delete(resultCafe);
        //then
        assertThat(cafeRepository.findById(resultCafe.getId()).isEmpty());
    }
}