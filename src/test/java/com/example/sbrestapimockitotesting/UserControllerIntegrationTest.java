package com.example.sbrestapimockitotesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterUser() throws Exception {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("testuser", "password", "test@example.com");
        User user = new User(1L, "testuser", "password", "test@example.com");

        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(user);

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isOk());

        verify(userService).registerUser(any(UserRegistrationDto.class));
    }

    @Test
    public void testRegisterUserAlreadyExists() throws Exception {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("testuser", "password", "test@example.com");

        doThrow(new RuntimeException("User already exists")).when(userService).registerUser(any(UserRegistrationDto.class));

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isBadRequest());

        verify(userService).registerUser(any(UserRegistrationDto.class));
    }
}
