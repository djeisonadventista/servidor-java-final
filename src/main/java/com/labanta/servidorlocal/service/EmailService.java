package com.labanta.servidorlocal.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailBoasVindas(
            String emailDestino,
            String nomeUtilizador) {

        // Criar um email simples
        SimpleMailMessage mensagem =
                new SimpleMailMessage();

        mensagem.setTo(emailDestino);

        mensagem.setSubject(
                "Bem-vindo ao Marketplace!"
        );

        mensagem.setText(
                "Olá " + nomeUtilizador + "!\n\n" +
                        "A tua conta foi criada com sucesso. " +
                        "Já podes fazer login e explorar os novos serviços.\n\n" +
                        "Com os melhores cumprimentos.\n" +
                        "Equipa do Marketplace"
        );

        // Enviar email
        mailSender.send(mensagem);
    }

    public void enviarOrcamentoPorEmail(
            String emailDestino,
            String nomeServico,
            Double precoConvertido,
            String moeda) {

        SimpleMailMessage mensagem =
                new SimpleMailMessage();

        mensagem.setTo(emailDestino);

        mensagem.setSubject(
                "O teu Orçamento do Marketplace"
        );

        String corpo =
                String.format(
                        "Olá!\n\n" +
                                "Aqui tens o orçamento solicitado para o serviço:\n\n" +
                                "Serviço: %s\n" +
                                "Preço Final: %.2f %s\n\n" +
                                "Este valor foi calculado com a taxa " +
                                "de câmbio em tempo real.\n" +
                                "Obrigado por usares o nosso Marketplace!",
                        nomeServico,
                        precoConvertido,
                        moeda
                );

        mensagem.setText(corpo);

        mailSender.send(mensagem);
    }

    public void enviarAlertaSeguranca(
            String emailDestino,
            String cidade,
            String pais) {

        SimpleMailMessage mensagem =
                new SimpleMailMessage();

        mensagem.setTo(emailDestino);

        mensagem.setSubject(
                "⚠️ Alerta de Segurança - Marketplace"
        );

        String corpo =
                "Aviso de Segurança!\n\n" +
                        "Detetámos uma nova atividade na tua conta " +
                        "do Marketplace a partir de:\n\n" +
                        "Cidade: " + cidade + "\n" +
                        "País: " + pais + "\n\n" +
                        "Se não foste tu, altera a tua password " +
                        "imediatamente!\n\n" +
                        "Com os melhores cumprimentos,\n" +
                        "Equipa de Segurança do Marketplace";

        mensagem.setText(corpo);

        mailSender.send(mensagem);
    }
}