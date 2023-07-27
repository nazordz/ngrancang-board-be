package com.unindra.ngrancang.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint{

	@Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
	
	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException
	) throws IOException, ServletException {
		log.error("Unauthorized error: {}", authException.getMessage());
		final String expired = (String) request.getAttribute("expired");
		if (expired != null) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			final Map<String, Object> body = new HashMap<>();
			body.put("status", "TOKEN_EXPIRED");
			body.put("message", authException.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), body);
			
		} else {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			final Map<String, Object> body = new HashMap<>();
			body.put("status", "INTERNAL_SERVER_ERROR");
			body.put("message", "An error occurred while processing the request.");

			final ObjectMapper mapper = new ObjectMapper();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			mapper.writeValue(response.getOutputStream(), body);
		}
	}

    
}
