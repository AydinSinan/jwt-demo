package com.mylogin.jwt.service;

import com.mylogin.jwt.entity.UserEntity;
import com.mylogin.jwt.model.UserModel;
import com.mylogin.jwt.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserModel register(UserModel userModel) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userModel, userEntity);
        userEntity = userRepository.save(userEntity);
        BeanUtils.copyProperties(userEntity, userModel);
        return userModel;
    }

    // this method actually does the validation for user existence.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(username);

        if(userEntity == null) {// here you can make a DB call with the help of repository and do the validation.
            throw new UsernameNotFoundException("User does not exist!");
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userEntity, userModel);

        return userModel;
    }
}
