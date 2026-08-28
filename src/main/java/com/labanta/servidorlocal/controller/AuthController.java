package com.labanta.servidorlocal.controller;

import com.labanta.servidorlocal.dto.GeoLocationResponse;
import com.labanta.servidorlocal.dto.LoginRequestDTO;
import com.labanta.servidorlocal.dto.RegistoRequestDTO;
import com.labanta.servidorlocal.model.Utilizador;
import com.labanta.servidorlocal.service.AuthService;
import com.labanta.servidorlocal.service.EmailService;
import com.labanta.servidorlocal.service.GeoService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final GeoService geoService;
    private final EmailService emailService;

    public AuthController(
            AuthService authService,
            GeoService geoService,
            EmailService emailService) {

        this.authService = authService;
        this.geoService = geoService;
        this.emailService = emailService;
    }

    @Operation(
            summary = "Registar novo utilizador",
            description = "Cria uma nova conta de utilizador na plataforma."
    )

    @PostMapping("/registar")
    public ResponseEntity<Map<String, Object>> registar(
            @RequestBody RegistoRequestDTO dados) {

        Utilizador utilizador =
                authService.registarUtilizador(dados);

        return ResponseEntity.ok(
                Map.of(
                        "mensagem",
                        "Utilizador registado com sucesso.",
                        "username",
                        utilizador.getUsername(),
                        "email",
                        utilizador.getEmail()
                )
        );
    }


    @Operation(
            summary = "Autenticar utilizador",
            description = "Autentica um utilizador através das suas credenciais e devolve um token JWT."
    )

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody LoginRequestDTO dados) {

        String token =
                authService.login(dados);

        return ResponseEntity.ok(
                Map.of(
                        "token",
                        token
                )
        );
    }

    @Operation(
            summary = "Enviar alerta de segurança de login",
            description = "Obtém a localização associada ao endereço IP informado e envia um alerta de segurança para o email indicado."
    )

    @PostMapping("/alerta-login")
    public ResponseEntity<String> alertaLogin(
            @RequestParam String email,
            @RequestParam String ip) {

        // 1. Consultar a localização do IP
        GeoLocationResponse localizacao =
                geoService.localizarIp(ip);

        // 2. Extrair cidade e país
        String cidade =
                localizacao.getCity();

        String pais =
                localizacao.getCountry_name();

        // 3. Enviar o alerta de segurança
        emailService.enviarAlertaSeguranca(
                email,
                cidade,
                pais
        );

        // 4. Responder ao Postman
        return ResponseEntity.ok(
                "Alerta de segurança processado!"
        );
    }
}