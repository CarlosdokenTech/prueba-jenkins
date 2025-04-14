package com.carlos.msvc.usuarios.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.carlos.msvc.usuarios.model.User;
import com.carlos.msvc.usuarios.model.UserDto;
import com.carlos.msvc.usuarios.services.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceImpl userServiceImpl;


    UserController(UserServiceImpl userServiceImpl){
      this.userServiceImpl = userServiceImpl;
    }

   //Conetamos para realizar el cambio
    @GetMapping
    public List<User> listarUsuario(){
      return userServiceImpl.getAllUsers();
        
    }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public User guardarUsuario(@RequestBody User user) {     
      
      return userServiceImpl.savedUser(user);
  }

 

  @GetMapping("/dto")
   List<UserDto> listarDtos(){
    return userServiceImpl.responceByUserDto();

   }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> obtenerUsuaioPorId(@PathVariable ("id") Long id){
    return userServiceImpl.getById(id)
          .map(user -> {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setNombre(user.getNombre());
            userDto.setApellido(user.getApellido());
            userDto.setEmail(user.getEmail());
            return  new ResponseEntity<>(userDto, HttpStatus.OK);
          })          
          .orElseGet(()-> ResponseEntity.notFound().build());

  }

  @PutMapping("/{id}")
  public ResponseEntity<User> actualizarUsuario(@PathVariable("id") Long id, @RequestBody User user){
    return userServiceImpl.getAllById(id)
      .map(userGuardado ->{
        userGuardado.setNombre(user.getNombre());
        userGuardado.setApellido(user.getApellido());
        userGuardado.setEmail(user.getEmail());
        userGuardado.setPass(user.getPass());
        userGuardado.setIsActive(user.getIsActive());
        
        
        User userActualizado = userServiceImpl.updateUser(userGuardado);
        return new ResponseEntity<>(userActualizado, HttpStatus.OK);

      })
      .orElseGet(()-> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> eliminarUsuario(@PathVariable("id") Long id){
   String result =  userServiceImpl.deleteUser(id);
   if("Usuario Eliminado exitosamente".equals(result)){
    return new ResponseEntity<String>(result,HttpStatus.OK);
   } else{
    return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
  }
    
 

}
  
}