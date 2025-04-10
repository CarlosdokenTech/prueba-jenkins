package com.carlos.msvc.usuarios.msvc_usuarios.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.carlos.msvc.usuarios.constantes.UserConstantes;
import com.carlos.msvc.usuarios.model.User;
import com.carlos.msvc.usuarios.services.UserServiceImpl;


@WebMvcTest
 class ControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

  @DisplayName("Test para guardar usuarios")
  @Test
  void testGuardarUsuarios()throws Exception{
    //Given
       User user = User.builder()
       .id(1L)
       .nombre("Carlos")
       .apellido("García")
       .email("charly@gmail.com")
       .build();

       given(userService.savedUser(any(User.class)))
       .willAnswer(invocation -> invocation.getArgument(0));
    //When

    ResultActions responce = mockMvc.perform(post("/api/user")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(user)));
    //Then
    responce.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre",is(user.getNombre())))
                .andExpect(jsonPath("$.apellido",is(user.getApellido())))
                .andExpect(jsonPath("$.email",is(user.getEmail())));
  }
                     
   
   @DisplayName("Tets para listar usuarios")
   @Test
   void testListarUsuarios() throws Exception{
    //Given
    List<User> listaUsers = new ArrayList<>();
    listaUsers.add(User.builder().nombre("Carlos").apellido("García").email("carlos@gmail.com").build());
    listaUsers.add(User.builder().nombre("Alan").apellido("García").email("alan@gmail.com").build());
    listaUsers.add(User.builder().nombre("Alejandro").apellido("García").email("alex@gmail.com").build());
    listaUsers.add(User.builder().nombre("Julieta").apellido("García").email("juli@gmail.com").build());
    listaUsers.add(User.builder().nombre("Elena").apellido("Toscano").email("elen@gmail.com").build());
    given(userService.getAllUsers()).willReturn(listaUsers);
    //When

    ResultActions responce = mockMvc.perform(get("/api/user"));
    //Then

    responce.andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.size()", is(listaUsers.size()) ));
   }

   @DisplayName("Test para obtener usuario por id")
   @Test
   void testObtenerUsuarioporId() throws Exception{
    //Given
    User user = User.builder()
    .id(1L)
    .nombre("Carlos")
    .apellido("Garcia")
    .email("car@gmail.com")
    .isActive(UserConstantes.FILTRADO)
    .build();

    //When
    when(userService.getById(1L)).thenReturn(Optional.of(user));
    //Then
    mockMvc.perform(get("/api/user/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1L))
           .andExpect(jsonPath("$.nombre").value("Carlos"))
           .andExpect(jsonPath("$.apellido").value("Garcia"))
           .andExpect(jsonPath("$.email").value("car@gmail.com"));
   }

   @DisplayName("Test para obtener un usuario por is vacoio")
   @Test
   void testObtenerUsuarioPorIdVacio() throws Exception{
    //Given
    
    //When
      when(userService.getById(2L)).thenReturn(Optional.empty());
    //Then
      mockMvc.perform(get("/api/user/2"))
            .andExpect(status().isNotFound())
            .andDo(print());
   }

   @DisplayName("Test para actualizar un usuario")
   @Test
   void testActualizarUsuario() throws Exception {
       // Given
       User usuario = User.builder()
           .id(1L)
           .nombre("Carlos")
           .apellido("Garcia")
           .email("car@gmail.com")
           .isActive(UserConstantes.FILTRADO)
           .build();

       User userActualizado = User.builder()
           .id(1L)
           .nombre("Charly")
           .apellido("Toscano")
           .email("charly@gmail.com")
           .isActive(UserConstantes.FILTRADO)
           .build();

       // When
       when(userService.getAllById(1L)).thenReturn(Optional.of(usuario));
       when(userService.updateUser(usuario)).thenReturn(userActualizado);

       // Then
       mockMvc.perform(put("/api/user/{id}", userActualizado.getId())
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(userActualizado)))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(userActualizado.getId()))
               .andExpect(jsonPath("$.nombre").value(userActualizado.getNombre()))
               .andExpect(jsonPath("$.apellido").value(userActualizado.getApellido()));
   }

   @DisplayName("Test para actualizar un usuario nulo")
   @Test
   void testActualzarUsuarioNulo() throws Exception{
    //Given
    
    User  usuarioActualizado = User.builder()
    .nombre("Julen")
    .apellido("Oliva")
    .email("j2@gmail.com")
    .build();
    given(userService.getById(1L)).willReturn(Optional.empty());
    given(userService.savedUser(any(User.class)))
                     .willAnswer(invocatio -> invocatio.getArgument(0));

    //When
    ResultActions responce  = mockMvc.perform(put("/api/user/{id}", usuarioActualizado.getId())
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(usuarioActualizado)));
    //Then
    responce.andExpect(status().isNotFound())
            .andDo(print());
   }


   @DisplayName("Test para eliminar un usuario exitosamente")
    @Test
    void testEliminarUsuarioExitosamente() throws Exception {
    // Given
      Long userId = 1L;
      String expectedMessage = "Usuario Eliminado exitosamente";

    // When
      when(userService.deleteUser(userId)).thenReturn(expectedMessage);

    // Then
      mockMvc.perform(delete("/api/user/{id}", userId))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedMessage));
}

  @DisplayName("Test para eliminar un usuario no encontrado")
  @Test
  void testEliminarUsuarioNoEncontrado() throws Exception {
    // Given
    Long userId = 1L;
    String expectedMessage = "Usuario no encontrado";

    // When
    when(userService.deleteUser(userId)).thenReturn(expectedMessage);

    // Then
    mockMvc.perform(delete("/api/user/{id}", userId))
            .andExpect(status().isNotFound())
            .andExpect(content().string(expectedMessage)); 
  }
}
