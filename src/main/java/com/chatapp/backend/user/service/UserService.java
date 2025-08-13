package com.chatapp.backend.user.service;

import com.chatapp.backend.user.dto.UserListItemDto;
import com.chatapp.backend.user.entity.User;
import com.chatapp.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.function.Function;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	// Spring Security method
	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(usernameOrEmail)
				.orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
						.orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail)));

		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
		);
	}

	// Business logic methods
	public Page<UserListItemDto> listUsersForChat(UUID currentUserId, String query, Pageable pageable) {
		Page<User> page = userRepository.searchAllExceptCurrent(currentUserId, normalizeQuery(query), pageable);
		return page.map(mapToUserListItemDto());
	}

	private String normalizeQuery(String query) {
		if (query == null || query.isBlank()) {
			return null;
		}
		return query.trim();
	}

	private Function<User, UserListItemDto> mapToUserListItemDto() {
		return user -> UserListItemDto.builder()
				.id(user.getId())
				.username(user.getUsername())
				.avatar(user.getAvatar())
				.createdAt(user.getCreatedAt())
				.build();
	}
}


