package com.carlos.msvc.usuarios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.carlos.msvc.usuarios.constantes.UserConstantes;
import com.carlos.msvc.usuarios.exception.ResourceNotFoundException;
import com.carlos.msvc.usuarios.model.User;
import com.carlos.msvc.usuarios.model.UserDto;
import com.carlos.msvc.usuarios.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
  
          UserServiceImpl(UserRepository userRepository){
             this.userRepository = userRepository;
          }

    @Override
    public User savedUser(User user) {
        Optional<User> usuarioGuardado = userRepository.findByEmail(user.getEmail());
        if(usuarioGuardado.isPresent()){
          throw new ResourceNotFoundException("El usuario con el email: " + user.getEmail()  +  " ya existe");
        }
      return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
      //List<User> suersList = userRepository.findAll()
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(Long id) {    //No admite usuarios inactivos
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent() && userOptional.get().getIsActive().equals(UserConstantes.FILTRADO)){
          User user = userOptional.get();  
        
            return Optional.of(user);
        }     
        return Optional.empty();
    }
    @Override
    public Optional<User> getAllById(Long id){
      return userRepository.findById(id);
    }

    

    @Override
    public User updateUser(User userUpdate) {
       return  userRepository.save(userUpdate);
    }

    @Override
    public String deleteUser(Long id) {
      Optional<User> userOptional = userRepository.findById(id);
      if(userOptional.isPresent()){
        User userDelete = userOptional.get();
        if(userDelete.getIsActive().equals(UserConstantes.FILTRADO)){
          userDelete.setIsActive(!UserConstantes.FILTRADO);
          userRepository.save(userDelete);
          return "Usuario eliminado exitosamente";
        } else {
          return "El usuario no existe";
         }          
      }else{
        return "El usuario especificado no existe";
      } 
    }
    

    @Override
    public List<UserDto> responceByUserDto(){
           List<User> usersList = userRepository.findAll().stream()
              .filter(s -> s.getIsActive().equals(UserConstantes.FILTRADO)).toList();

      return usersList.stream().map(s->{
        UserDto userDto = new UserDto();
        userDto.setId(s.getId());
        userDto.setNombre(s.getNombre());
        userDto.setApellido(s.getApellido());
        userDto.setEmail(s.getEmail());

        return userDto;
      }).toList();

    
    }

  }

   
