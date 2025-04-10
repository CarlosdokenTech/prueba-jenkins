package com.carlos.msvc.usuarios.msvc_usuarios;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.carlos.msvc.usuarios.services.UserService;

@SpringBootTest
class MsvcUsuariosApplicationTests {

	 @Autowired
    private UserService userService; // Inyectamos el servicio a probar

    @Test
    void contextLoads() {
        
        assertNotNull(userService); 
    }

}
