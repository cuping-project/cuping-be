package com.cuping.cupingbe.global.security;


import com.cuping.cupingbe.service.MediatorImpl;
import com.cuping.cupingbe.service.MemberService;
import com.cuping.cupingbe.service.MyPageService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cuping.cupingbe.entity.User;
import com.cuping.cupingbe.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final MyPageService myPageService;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = myPageService.checkUserId(userId);

		return new UserDetailsImpl(user, user.getUserId());
	}
}