package com.cuping.cupingbe.service;

import com.cuping.cupingbe.dto.MemberLoginRequestDto;
import com.cuping.cupingbe.dto.MemberSignupRequestDto;
import com.cuping.cupingbe.dto.TokenDto;
import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.entity.UserRoleEnum;
import com.cuping.cupingbe.global.jwt.JwtUtil;
import com.cuping.cupingbe.global.redis.util.RedisUtil;
import com.cuping.cupingbe.global.util.Message;
import com.cuping.cupingbe.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private UtilService utilService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RedisUtil redisUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testSignup() throws Exception {
        MemberSignupRequestDto requestDto = new MemberSignupRequestDto();
        requestDto.setUserId("jaykim");
        requestDto.setPassword("1408adad");
        requestDto.setNickname("lam");
        //레포지에서 jaykim이 없어야 하니까 optional.empty반환
        when(userRepository.findByUserId("jaykim")).thenReturn(Optional.empty());
        //레포지에서 lam이 없어야 하니까 optional.empty반환
        when(userRepository.findByNickname("lam")).thenReturn(Optional.empty());
        //비밀번호를 암호화하려는 시도가 있을때 encodedPassword반환
        when(passwordEncoder.encode("1408adad")).thenReturn("encodedPassword");
        //save메소드가 user객체에 대해 호출되면 -> 새로운 user 객체 반환(실제 데이터베이스 사용x
        when(userRepository.save(any(User.class))).thenReturn(new User());
        //signup 메소드 호출 -> 예외발생시키지 않는지 검증
        assertDoesNotThrow(() -> memberService.signup("user", requestDto));
    }

    @Test
    public void testLogin() {
        TokenDto mockTokenDto = new TokenDto("accessToken", "refreshToken");
        // Given
        MemberLoginRequestDto loginRequestDto = new MemberLoginRequestDto();
        loginRequestDto.setUserId("jaykim");
        loginRequestDto.setPassword("1408adad");
        User testUser = new User("jaykim", "encodedPassword", "lam", UserRoleEnum.USER);
        HttpServletResponse response = new MockHttpServletResponse();
        //checkUserId메서드가 jaykim과 함꼐 호출 -> testUser반환
        when(utilService.checkUserId("jaykim")).thenReturn(testUser);
        //void 반환하는 메서드는 doNothing을 써야한다. set된 비밀번호랑 testUser의 비밀번호와 함께 호출될떄 아뭇것도 수행x
        doNothing().when(utilService).checkUserPassword("1408adad", testUser.getPassword());
        //token반환
        when(jwtUtil.creatAllToken(testUser.getUserId(), testUser.getRole())).thenReturn(mockTokenDto);
        doNothing().when(jwtUtil).setCookies(any(HttpServletResponse.class), eq(mockTokenDto));
        doNothing().when(redisUtil).set(eq(testUser.getUserId()), eq(mockTokenDto.getRefreshToken()), eq(JwtUtil.REFRESH_TIME));
        // When
        ResponseEntity<Message> result = memberService.login(loginRequestDto, response);
        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("로그인 성공", result.getBody().getMessage());
    }
    @Test
    public void testLogout() {
        User testUser = new User("jaykim", "1408adad", "lam", UserRoleEnum.USER);
        assertDoesNotThrow(() -> memberService.logout(testUser));
    }

}
