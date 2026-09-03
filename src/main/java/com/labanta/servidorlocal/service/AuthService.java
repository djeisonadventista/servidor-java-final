package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.dto.LoginRequestDTO;
import com.labanta.servidorlocal.dto.RegistoRequestDTO;
import com.labanta.servidorlocal.exception.CredenciaisInvalidasException;
import com.labanta.servidorlocal.exception.UtilizadorExistenteException;
import com.labanta.servidorlocal.model.Utilizador;
import com.labanta.servidorlocal.repository.UtilizadorRepository;
import com.labanta.servidorlocal.security.JwtService;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtilizadorRepository utilizadorRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(
            UtilizadorRepository utilizadorRepository,
            JwtService jwtService, EmailService emailService) {

        this.utilizadorRepository = utilizadorRepository;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public Utilizador registarUtilizador(
            RegistoRequestDTO dados) {

        if (utilizadorRepository
                .findByUsername(dados.getUsername())
                .isPresent()) {

            throw new UtilizadorExistenteException(
                    "Este username já está em uso, por favor escolha outro."
            );
        }

        Utilizador novoUtilizador = new Utilizador(
                dados.getUsername(),
                dados.getPassword(),
                dados.getEmail()
        );

        //emailService.enviarEmailBoasVindas(novoUtilizador.getEmail(), novoUtilizador.getUsername());
        return utilizadorRepository.save(novoUtilizador);
    }

    public String login(LoginRequestDTO dados) {

        Utilizador utilizador =
                utilizadorRepository
                        .findByUsername(dados.getUsername())
                        .orElseThrow(() ->
                                new CredenciaisInvalidasException(
                                        "Username ou password inválidos."
                                )
                        );

        if (!utilizador.getPassword()
                .equals(dados.getPassword())) {

            throw new CredenciaisInvalidasException(
                    "Username ou password inválidos."
            );
        }

        return jwtService.gerarToken(
                utilizador.getUsername()
        );
    }
}