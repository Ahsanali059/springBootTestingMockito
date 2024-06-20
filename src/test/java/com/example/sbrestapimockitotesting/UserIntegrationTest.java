package com.example.sbrestapimockitotesting;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/auth";
        userRepository.deleteAll();
    }

    @Test
    public void testRegister() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        ResponseEntity<User> response = restTemplate.postForEntity(baseUrl + "/register", user, User.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getUsername()).isEqualTo("testuser");
        assertThat(userRepository.existsByUsername("testuser"));
    }

    @Test
    public void testLoginSuccess() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        userRepository.save(user);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/login", HttpMethod.POST, request, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Login successful");
    }

    @Test
    public void testLoginFailure() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("wrongpassword");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/login", HttpMethod.POST, request, String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
        assertThat(response.getBody()).isEqualTo("Invalid credentials");
    }
}