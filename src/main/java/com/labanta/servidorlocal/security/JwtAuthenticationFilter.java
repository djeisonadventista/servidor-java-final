package com.labanta.servidorlocal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);

            //ignorar o token vazios ou "undefined" (frontend mal configurado)
            if(token.isEmpty() || token.equals("undefined")) {
                filterChain.doFilter(request, response);
                return;
            }

            try {
                // Extrair o username do token (isto tambem valida a assinatura e a expiracao)
                String username = jwtService.extrairUsername(token);

                //Se o username é valido e ainda nao há autenticacao no contexto
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    //Dizer ao spring que este utilizador está autenticado
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                //Token invalido ou expirado - nao autenticar, o Spring vai devolver 401
            }

            filterChain.doFilter(request, response);

        }
}