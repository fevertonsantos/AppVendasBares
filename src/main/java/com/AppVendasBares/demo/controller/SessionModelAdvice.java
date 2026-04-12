package com.AppVendasBares.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class SessionModelAdvice {

    @ModelAttribute
    public void addSessionAttributes(HttpSession session, Model model) {
        Object perfil = session.getAttribute("perfil");
        Object empresaId = session.getAttribute("empresaId");
        Object nomeUsuario = session.getAttribute("nomeUsuario");
        Object mesaId = session.getAttribute("mesaId");

        model.addAttribute("perfil", perfil != null ? perfil.toString() : "CLIENTE");
        model.addAttribute("empresaId", empresaId != null ? empresaId : 1L);
        model.addAttribute("nomeUsuario", nomeUsuario != null ? nomeUsuario.toString() : null);
        model.addAttribute("mesaId", mesaId != null ? mesaId : 1L);
    }
}
