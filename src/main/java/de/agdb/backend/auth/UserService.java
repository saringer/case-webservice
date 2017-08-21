package de.agdb.backend.auth;

import de.agdb.backend.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    JdbcTemplate jdbcTemp;


    public Users getUserbyName(String userName) {
        String sql = "SELECT * FROM users WHERE USERNAME = ?";

        Users user = null;
        try {
            user = jdbcTemp.queryForObject(sql,
                    new Object[]{userName}, BeanPropertyRowMapper.newInstance(Users.class));

        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        // fetch user from DB
        Users databaseFetchedUser = getUserbyName(username);
        if (databaseFetchedUser != null) {
            authorities.add(new SimpleGrantedAuthority("CLIENT"));
            User user = new User(username, databaseFetchedUser.getPassword(), true, true, false, false, authorities);
            return user;
        } else {
            return null;
        }


    }


}
