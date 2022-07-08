package com.codedifferently.tankofamerica.domain.user.services;

import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.models.User;
import com.codedifferently.tankofamerica.domain.user.repos.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"
})
@ExtendWith(SpringExtension.class)
public class UserServiceImpTest {

    private User mockUser;
    private User mockUser2;

    private ArrayList<User> users;

    @BeforeEach
    void setUp() {
        mockUser = new User("Tariq", "Hook", "email@email", "pass");
        mockUser.setId(1l);
        mockUser2 = new User("Hughie", "Campbell", "email@email", "pass");
        mockUser2.setId(2l);
        users = new ArrayList<>();
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @Test
    public void getUserByIdTest01() throws UserNotFoundException {
        // Given
        User mockUser = new User("Tariq", "Hook", "email@email", "pass");
        mockUser.setId(1l);
        BDDMockito.doReturn(Optional.of(mockUser)).when(userRepo).findById(1l);
        User actualUser = userService.getById(1l);
        Assertions.assertEquals(mockUser, actualUser);
    }

    @Test
    public void getUserByIdTest02(){
        BDDMockito.doReturn(Optional.empty()).when(userRepo).findById(1l);
        Assertions.assertThrows(UserNotFoundException.class, ()->{
            userService.getById(1l);
        });
    }

    @Test
    public void getAllUsers01(){
        users.add(mockUser);
        users.add(mockUser2);
        BDDMockito.doReturn(users).when(userRepo).findAll();
        String expected = "1 Tariq Hook email@email pass\n" +
                "2 Hughie Campbell email@email pass";
        String actual = userService.getAllUsers();
        Assertions.assertEquals(expected, actual);
    }

}
