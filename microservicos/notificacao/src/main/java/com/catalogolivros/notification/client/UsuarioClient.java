package com.catalogolivros.notification.client;

import com.catalogolivros.notification.dto.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuario-service-notif", url = "http://usuarios:8080")
public interface UsuarioClient {

    @GetMapping("/usuarios/{id}")
    UsuarioDTO buscarPorId(@PathVariable Long id);
}
