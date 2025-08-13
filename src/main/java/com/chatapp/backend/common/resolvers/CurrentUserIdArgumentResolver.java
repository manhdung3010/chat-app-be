package com.chatapp.backend.common.resolvers;

import com.chatapp.backend.common.annotations.CurrentUserId;
import com.chatapp.backend.user.entity.User;
import com.chatapp.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

/**
 * ArgumentResolver để tự động inject current user ID từ SecurityContext.
 *
 * Hỗ trợ 2 trường hợp:
 * - Principal name là UUID (token chứa userId)
 * - Principal name là username/email (token chứa username) → tra DB lấy userId
 */
@Component
@RequiredArgsConstructor
public class CurrentUserIdArgumentResolver implements HandlerMethodArgumentResolver {

	private final UserRepository userRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(CurrentUserId.class)
				&& parameter.getParameterType().equals(UUID.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
								 ModelAndViewContainer mavContainer,
								 NativeWebRequest webRequest,
								 WebDataBinderFactory binderFactory) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new RuntimeException("User not authenticated");
		}

		String principalName = authentication.getName();
		try {
			return UUID.fromString(principalName);
		} catch (IllegalArgumentException ignored) {
			// Not a UUID → try resolve by username or email
			User user = userRepository.findByUsername(principalName)
					.orElseGet(() -> userRepository.findByEmail(principalName)
								.orElseThrow(() -> new RuntimeException("Authenticated user not found")));
			return user.getId();
		}
	}
}

