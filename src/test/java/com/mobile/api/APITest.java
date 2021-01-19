package com.mobile.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobile.api.controller.APIController;
import com.mobile.api.model.User;
import com.mobile.api.service.JWTService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;


@WebMvcTest(APIController.class)
@ActiveProfiles("test")
public class APITest {
	
	@MockBean
	JWTService jwtServiceMock;
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	MockMvc mockMvc;

	@Test
	public void post_createsNewVehicle_andReturnsObjWith201() throws Exception {
		User user = new User();
		user.setUsername("user");

		Mockito.when(jwtServiceMock.loadUserByUsername("user")).thenReturn(new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),new ArrayList<>()));

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(this.mapper.writeValueAsBytes(user));

		mockMvc.perform(builder).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.content()
				.string(this.mapper.writeValueAsString(user)));
	}

}