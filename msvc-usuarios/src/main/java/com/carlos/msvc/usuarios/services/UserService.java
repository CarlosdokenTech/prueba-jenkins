package com.carlos.msvc.usuarios.services;

import java.util.List;
import java.util.Optional;

import com.carlos.msvc.usuarios.model.User;
import com.carlos.msvc.usuarios.model.UserDto;


public interface UserService {
    
    User savedUser(User user);

    List<User> getAllUsers();

    Optional<User> getById(Long id);

    Optional<User> getAllById(Long id);

    User updateUser(User userUpdate);

    String deleteUser(Long id);

    List<UserDto>  responceByUserDto();

    
}
