package com.codedifferently.tankofamerica.domain.user.services;

import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.models.User;
import com.codedifferently.tankofamerica.domain.user.models.UserRoles;
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
        mockUser.setRole(UserRoles.USER);
        mockUser2 = new User("Hughie", "Campbell", "email@email", "pass");
        mockUser2.setId(2l);
        mockUser2.setRole(UserRoles.USER);
        users = new ArrayList<>();
    }

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepo userRepo;

    @Test
    public void getUserByIdTest01() throws UserNotFoundException {
        // Given
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
    public void getUserByEmailTest01() throws UserNotFoundException {
        BDDMockito.doReturn(Optional.of(mockUser)).when(userRepo).findByEmail("fake@fakemail.com");
        User actualUser = userService.getByEmail("fake@fakemail.com");
        Assertions.assertEquals(mockUser, actualUser);
    }

    @Test
    public void getUserByEmailTest02() {
        BDDMockito.doReturn(Optional.empty()).when(userRepo).findByEmail("");
        Assertions.assertThrows(UserNotFoundException.class, ()-> {
            userService.getByEmail("");
        });
    }

    @Test
    public void getAllUsers01(){
        users.add(mockUser);
        users.add(mockUser2);
        BDDMockito.doReturn(users).when(userRepo).findAll();
        String expected = "1 USER Tariq Hook email@email\n" +
                "2 USER Hughie Campbell email@email";
        String actual = userService.getAllUsers();
        Assertions.assertEquals(expected, actual);
    }

}
