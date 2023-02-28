package com.example.demo.auth;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.demo.security.UserRole.*;

@Repository("fakeRepository")
public class AppUserRepository implements AppUserDao {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserRepository(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<AppUser> selectAppUserByUsername(String username) {
        return getAppUsers().stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
    }

    private List<AppUser> getAppUsers() {
        List<AppUser> appUsers = Lists.newArrayList(
                new AppUser(
                        "tommy",
                        passwordEncoder.encode("password"),
                        STUDENT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new AppUser(
                        "jake",
                        passwordEncoder.encode("password"),
                        ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new AppUser(
                        "john",
                        passwordEncoder.encode("password"),
                        ADMINTRAINEE.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                )
        );
        return appUsers;
    }
}
