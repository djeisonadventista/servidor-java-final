package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.dto.ExchangeRateResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeService {

    private final RestTemplate restTemplate;

    public ExchangeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Double converterPreco(Double precoEuros, String moedaDestino) {
        String url = "https://api.exchangerate-api.com/v4/latest/EUR";

        //Fazer a chamada HTTP (GET) á internet e guarda no nosso DTO
        ExchangeRateResponse resposta = restTemplate.getForObject(url, ExchangeRateResponse.class);

        //Ir ao mapa procurar a taxa da moedsa perdida (ex: CVE)
        if (resposta != null && resposta.getRates().containsKey(moedaDestino)) {
            Double taxa = resposta.getRates().get(moedaDestino);
            return precoEuros * taxa; //Preco original * Taxa de Câmbio
        }

        throw new RuntimeException("Moeda não suportada ou API indesponível");
    }
}
