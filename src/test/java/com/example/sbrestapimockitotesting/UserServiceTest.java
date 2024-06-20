package com.example.sbrestapimockitotesting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterUser() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("testuser", "password", "test@example.com");

        doReturn(false).when(userRepository).existsByUsername("testuser");
        doReturn(false).when(userRepository).existsByEmail("test@example.com");
        doReturn(new User()).when(userRepository).save(any(User.class));

        User result = userService.registerUser(userRegistrationDto);

        assertNotNull(result);
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("testuser", "password", "test@example.com");

        doReturn(true).when(userRepository).existsByUsername("testuser");

        assertThrows(RuntimeException.class, () -> {
            userService.registerUser(userRegistrationDto);
        });

        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }
}
