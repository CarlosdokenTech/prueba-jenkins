package com.carlos.msvc.usuarios.msvc_usuarios.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carlos.msvc.usuarios.constantes.UserConstantes;
import com.carlos.msvc.usuarios.exception.ResourceNotFoundException;
import com.carlos.msvc.usuarios.model.User;
import com.carlos.msvc.usuarios.model.UserDto;
import com.carlos.msvc.usuarios.repository.UserRepository;
import com.carlos.msvc.usuarios.services.UserServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
 class UsuariosServicesTest {

    @Mock
    private  UserRepository userRepository;
    @InjectMocks
    private  UserServiceImpl userService;
    private User usuario;

    @BeforeEach
    void setup(){
        usuario = User.builder()
                .id(1L)
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();

    }

    @DisplayName("Test para guardar un empleado")
    @Test
        void testGuardarUsuario(){

            //Given
            given(userRepository.findByEmail(usuario.getEmail()))
             .willReturn(Optional.empty());           
            given(userRepository.save(usuario)).willReturn(usuario);
             //When
              User usuarioGuardado = userService.savedUser(usuario);
            //Then
            assertThat(usuarioGuardado).isNotNull();
            
        }
    
        @DisplayName("Guardar usuario con ThrowException")
        @Test
        void gusrdarUsuarioConThrowExcetion(){
        //Given
             given(userRepository.findByEmail(usuario.getEmail()))
             .willReturn(Optional.of(usuario));
        //When
        assertThrows(ResourceNotFoundException.class, () ->{
            userService.savedUser(usuario);
        });
        //Then
        verify(userRepository, never()).save(any(User.class));

        
        }

        @DisplayName("Test para listar usuarios")
        @Test
        void testListarUsuarios(){
            
        //Given
        User usuario1 = User.builder()
              .id(1L)
              .nombre("Carlos")
              .apellido("Garcia")
              .email("carlos@gmail.com")
              .build();

             given(userRepository.findAll()).willReturn(List.of(usuario, usuario1));
        //When
        List<User> usuarios =   userService.getAllUsers();
        
        //Then
        assertThat(userService.getAllUsers()).isNotNull();
        assertEquals(2, usuarios.size());
        
        }
        @DisplayName("Test para listar usuarios con Collections")
        @Test
        void testListarUsuariosConCollectionsVacia(){
            //Given
           
             given(userRepository.findAll()).willReturn(Collections.emptyList());
            //When
            List<User> usuarios = userService.getAllUsers();
            //Then
            assertThat(userService.getAllUsers()).isNotNull();
            assertEquals(0, usuarios.size() );

        }
        @DisplayName("Test para actualizar un usuario")
        @Test
        void testActualizarUsuario()  {
            // Given
            User usuariox = User.builder()
                .id(1L)
                .nombre("Carlos")
                .apellido("Garcia")
                .email("car@gmail.com")
                .isActive(UserConstantes.FILTRADO)
                .build();
                
                //When
                usuariox.setNombre("Alan");
               
               //Then
               when(userRepository.save(usuariox)).thenReturn(usuariox);
               User actual = userRepository.save(usuariox);
               assertNotNull(actual);
               assertEquals("Alan", actual.getNombre());
               verify(userRepository).save(usuariox);
        }



        @DisplayName("Test para obtener usuarios activos por id")
        @Test
        void testGetById_ActiveUser() {
            //Given
        User activeUser = new User();
        activeUser.setId(1L);
        activeUser.setIsActive(UserConstantes.FILTRADO);
        //When
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser));

        Optional<User> result = userService.getById(1L);
        //Then
        assertEquals(true, result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @DisplayName("Test para obtener un usuario inactivo")
    @Test
     void testGetById_InactiveUser() {
        //Given
        User inactiveUser = new User();
        inactiveUser.setId(2L);
        inactiveUser.setIsActive(!UserConstantes.FILTRADO);
        //When
        when(userRepository.findById(2L)).thenReturn(Optional.of(inactiveUser));

        Optional<User> result = userService.getById(2L);
        //Then
        assertFalse(result.isPresent());
    }

    @DisplayName("Test para obtener usuario inactivo")
    @Test
     void testParaObtenerUsuarioInactivo(){
        //Given
        User user = new User();
        user.setId(2L);
        user.setIsActive(!UserConstantes.FILTRADO);     
        
        //When
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        //Then
         String result = userService.deleteUser(2L);
        assertEquals("El usuario no existe", result);
        verify(userRepository, never()).save(user);
    }

   @DisplayName("Test para obtener un susario inexistenmte por id")
    @Test
     void testGetById_UserNotFound() {
        //Given
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        //When
        Optional<User> result = userService.getById(3L);
        //Then
        assertFalse(result.isPresent());
    }

    @DisplayName("Test para obtenmer un usuario por id normal")
    @Test
    void testParaobtenerusuarioPorIdNormal(){
        //Given
        given(userRepository.findById(1L)).willReturn(Optional.of(usuario));

        //When
        User usuarioLoco = userService.getAllById(usuario.getId()).get();
        //Then
        assertThat(usuarioLoco).isNotNull();
    }
    @DisplayName("Test para actualizar un usuario")
    @Test
    void testParaSctializarUsuario(){
        //Given
        given(userRepository.save(usuario)).willReturn(usuario);
        usuario.setNombre("Alejandro");
        usuario.setEmail("alex@gmail.com");
        //When
        User usuarioActualizado = userService.savedUser(usuario);
        //Then
        assertEquals("Alejandro",usuarioActualizado.getNombre());
        assertEquals("alex@gmail.com",usuarioActualizado.getEmail());
    }

    @DisplayName("Test para borrado logico")
    @Test
    void testParaBotrradoLogico(){
        //Given
        User userActive = new User();
        userActive.setId(1L);
        userActive.setIsActive(UserConstantes.FILTRADO);
        
        //When
         when(userRepository.findById(1L)).thenReturn(Optional.of(userActive));
         String result = userService.deleteUser(1L);
        //Then

        assertEquals("Usuario eliminado exitosamente", result);
        assertEquals(!UserConstantes.FILTRADO, userActive.getIsActive());
        verify(userRepository).save(userActive);
    }
    @Test
    void testDeleteUser_UserDoesNotExist() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        String result = userService.deleteUser(3L);

        assertEquals("El usuario especificado no existe", result);
    }

    @DisplayName("Test para convertir usuarios activos a dtos")
    @Test
    void testParaConvertirUsuariosActivosAdtos(){
        //Given
            User activeUser1 = new User(1L, "Carlos", "Garcia", "carlos@gmail.com", "garcia", UserConstantes.FILTRADO );
            User activeUser2 = new User(2L, "Elena", "Toscano", "elena@gmail.com", "toscano", UserConstantes.FILTRADO );
            User inActiveUser = new User(3L, "Robert", "Lukin", "gaby@gmail.com", "lukin", !UserConstantes.FILTRADO );
        //When
        when(userRepository.findAll()).thenReturn(Arrays.asList(activeUser1, activeUser2, inActiveUser));
        List<UserDto> result = userService.responceByUserDto();
        //Then

        assertEquals(1L, result.get(0).getId());
        assertEquals("Carlos", result.get(0).getNombre());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Elena", result.get(1).getNombre());
        assertEquals(2, result.size());
        
    } 


}
