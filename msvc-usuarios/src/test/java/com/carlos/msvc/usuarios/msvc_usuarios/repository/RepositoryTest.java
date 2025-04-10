package com.carlos.msvc.usuarios.msvc_usuarios.repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.carlos.msvc.usuarios.constantes.UserConstantes;
import com.carlos.msvc.usuarios.model.User;
import com.carlos.msvc.usuarios.repository.UserRepository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")

 class RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User usuario;

    @BeforeEach
    void setup() {
        usuario = User.builder()
            .nombre("Crhistian")
            .apellido("Ramireez")
            .email("cr@gmail.com")
            .isActive(UserConstantes.FILTRADO)
            .build();
            usuario = userRepository.save(usuario);
    }

    @DisplayName("Test para guardar usuarios")
    @Test
    void testGuardarUsuarios() {
        // Given
        User usuario1 = User.builder()
            .nombre("Pepe")
            .apellido("Lopez")
            .email("p1@gmail.com")
            .build();

        // When
        User usuarioGuardado1 = userRepository.save(usuario1);
        User usuarioGuardado2 = userRepository.save(usuario);

        // Then
        assertThat(usuarioGuardado1).isNotNull();
        assertThat(usuarioGuardado1.getId()).isGreaterThan(0);
        assertThat(usuarioGuardado2).isNotNull();
        assertThat(usuarioGuardado2.getId()).isGreaterThan(0);
    }   

    @DisplayName("Test para listar usuarios")
    @Test
    void testParaListarUsuarios(){
    //Given
        User usuario1 = User.builder()
        .nombre("Alana")
        .apellido("Garcia")
        .email("alan@gmail.com")
        .build();
        User usuarioGuardado = userRepository.save(usuario1);
       
        //When
       List<User> listadoUsuarios = userRepository.findAll();
        //Then
        assertThat(listadoUsuarios).isNotNull();
        assertEquals(2,listadoUsuarios.size() );
    }

    @DisplayName("Test ara obtener un usuario por id")
    @Test
    void testObtenerEmpleadoPorId(){

        //Given
          userRepository.save(usuario);
        //When
        User userDb = userRepository.findById(usuario.getId()).get();
        //Then
        assertThat(userDb).isNotNull();
        
    }

    @DisplayName("Test para actualizar un usuario")
    @Test
    void testActualizarUsuarios(){
        //Given
         userRepository.save(usuario);
        //When
        User usuarioGuardado = userRepository.findById(usuario.getId()).get();
              usuarioGuardado.setNombre("Alex");
              usuarioGuardado.setApellido("Gartos");
              usuarioGuardado.setEmail("alex@gmail.com");
              User usuarioActualizado = userRepository.save(usuarioGuardado);
        //Then
         assertEquals("Alex", usuarioActualizado.getNombre() );
         assertEquals("alex@gmail.com", usuarioActualizado.getEmail() );
    }
    
    @DisplayName("Test para borrado logico a un usuario")
    @Test
    void testEliminarUsuarios(){
      // Given
      Optional<User> userOptional = userRepository.findById(usuario.getId());
      assertThat(userOptional).isPresent();
      User userToBeDeleted = userOptional.get();
      userToBeDeleted.setIsActive(!UserConstantes.FILTRADO);

      // When
      userRepository.save(userToBeDeleted);

      // Then
      Optional<User> deletedUserOptional = userRepository.findById(usuario.getId());
      assertThat(deletedUserOptional).isPresent();
      assertThat(deletedUserOptional.get().getIsActive()).isEqualTo(!UserConstantes.FILTRADO);
  }


     


}
