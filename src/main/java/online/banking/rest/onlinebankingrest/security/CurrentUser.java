package online.banking.rest.onlinebankingrest.security;

import online.banking.rest.onlinebankingrest.entity.User;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;


public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public CurrentUser(User user) {
        super(user.getEmail(),
                user.getPassword(),
                createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
