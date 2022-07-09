package com.codedifferently.tankofamerica.domain.account.services;

import com.codedifferently.tankofamerica.domain.account.models.Account;
import com.codedifferently.tankofamerica.domain.account.repo.AccountRepo;
import com.codedifferently.tankofamerica.domain.user.exceptions.UserNotFoundException;
import com.codedifferently.tankofamerica.domain.user.models.User;
import com.codedifferently.tankofamerica.domain.user.repos.UserRepo;
import com.codedifferently.tankofamerica.domain.user.services.UserService;
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
import java.util.UUID;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"
})
@ExtendWith(SpringExtension.class)
public class AccountServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepo userRepo;
    @Autowired
    private AccountService accountService;
    @MockBean
    private AccountRepo accountRepo;

    private Account mockAccount;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setFirstName("Luis");
        mockUser.setLastName("Adorno");
        mockUser.setId(1L);
        mockUser.setEmail("legend@fakemail.com");
        mockUser.setPassword("123");
        mockAccount = new Account("Checkings");
        mockAccount.setOwner(mockUser);
        mockAccount.setId(UUID.fromString("8a984aa2-5b1e-4bd4-9e82-d773b4c95c4b"));
    }

    @Test
    public void getAllAccountsFromUserTest01() throws UserNotFoundException {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(mockAccount);
        BDDMockito.doReturn(Optional.of(mockUser)).when(userRepo).findById(1L);
        BDDMockito.doReturn(accounts).when(accountRepo).findByOwner(mockUser);
        String expected = mockAccount.toString();
        String actual = accountService.getAllFromUser(1L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllAccountsTest02() {
        BDDMockito.doReturn(Optional.empty()).when(userRepo).findById(1L);
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            accountService.getAllFromUser(1L);
        });
    }

}
