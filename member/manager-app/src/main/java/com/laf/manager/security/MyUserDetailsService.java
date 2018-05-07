package com.laf.manager.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("登录用户名：{}", username);

//        return new User(username, "123456", AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        String password = passwordEncoder.encode("111");

        log.info("密码：{}", password);

//        if (StringUtils.isEmpty(username)) username = "laf";

        if (StringUtils.isEmpty(username) || (!username.equals("admin") && !username.equals("user1"))) {
            throw new UsernameNotFoundException("not found");
        }

        return new User(username, password, true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList(username));
    }
}
