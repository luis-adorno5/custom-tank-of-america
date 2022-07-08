package com.codedifferently.tankofamerica.domain.utils;
import com.codedifferently.tankofamerica.domain.user.models.User;
import org.springframework.stereotype.Component;

@Component
public class LoginHelper {
    private Boolean signedIn = false;
    private User currentUser = null;

    public LoginHelper() {
    }

    public Boolean getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(Boolean signedIn) {
        this.signedIn = signedIn;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
