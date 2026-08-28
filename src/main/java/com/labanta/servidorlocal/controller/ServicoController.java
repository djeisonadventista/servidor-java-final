package com.labanta.servidorlocal.controller;

import com.labanta.servidorlocal.dto.ServicoResponseDTO;
import com.labanta.servidorlocal.model.ServicoModel;
import com.labanta.servidorlocal.service.EmailService;
import com.labanta.servidorlocal.service.ExchangeService;
import com.labanta.servidorlocal.service.FileStorageService;
import com.labanta.servidorlocal.service.ServicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/servicos")
public class ServicoController {

    private final ServicoService servicoService;
    private final ExchangeService exchangeService;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;


    public ServicoController(
            ServicoService servicoService, ExchangeService exchangeService, EmailService emailService, FileStorageService fileStorageService) {

        this.servicoService = servicoService;
        this.exchangeService = exchangeService;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
    }

    @Operation
            (summary = "Listar todos os servicos",
                    description = "Rota para listar todos os servicos existentes na plataforma")

    @GetMapping
    public Page<ServicoModel> listarTodos(
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "id",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
            ) {

        return servicoService.listarTodos(pageable);
    }

    @Operation(
            summary = "Pesquisar serviços",
            description = "Pesquisa serviços através de um termo presente no título do serviço."
    )
    @SecurityRequirement(name = "BearerAuth")

    @GetMapping("/pesquisa")
    public List<ServicoModel> pesquisarServicos(
            @RequestParam String termo) {

        return servicoService
                .pesquisarPorTitulo(termo);
    }

    @Operation(
            summary = "Consultar serviço por ID",
            description = "Obtém os dados de um serviço específico através do seu identificador."
    )

    @GetMapping("/{id}")
    public ServicoModel buscarServicoPorId(
            @PathVariable Long id) {

        return servicoService
                .buscarServicoPorId(id);
    }

    @Operation(
            summary = "Criar um novo serviço",
            description = "Cria e guarda um novo serviço na plataforma."
    )
    @SecurityRequirement(name = "BearerAuth")

    @PostMapping
    public ServicoModel criarServico(
            @RequestBody ServicoModel novoServico) {

        return servicoService.criarServico(novoServico);
    }

    @Operation(
            summary = "Aplicar desconto aos serviços ativos",
            description = "Aplica uma percentagem de desconto aos serviços que se encontram ativos e devolve os seus preços atualizados."
    )
    @SecurityRequirement(name = "BearerAuth")

    @PostMapping("/aplicar-desconto")
    public List<ServicoResponseDTO> aplicarDesconto(
            @RequestParam Double percentagem) {

        List<ServicoModel> servicos =
                servicoService
                        .aplicarDescontoEmAtivos(
                                percentagem
                        );

        List<ServicoResponseDTO> resposta =
                new ArrayList<>();

        for (ServicoModel servico : servicos) {

            ServicoResponseDTO dto =
                    new ServicoResponseDTO(
                            servico.getTitulo(),
                            servico.getPrecoComDesconto()
                    );

            resposta.add(dto);
        }

        return resposta;
    }

    @Operation(
            summary = "Calcular e enviar orçamento",
            description = "Obtém o serviço, converte o preço para a moeda indicada e envia o orçamento calculado para o email especificado."
    )
    @SecurityRequirement(name = "BearerAuth")

    @PostMapping("/{id}/orcamento")
    public String pedirOrcamento(@PathVariable Long id,
                                 @RequestParam String emailDestino,
                                 @RequestParam(defaultValue = "CVE") String moeda) {

        // 1. Ir à Base de Dados buscar o Serviço
        ServicoModel servico = servicoService.buscarServicoPorId(id);

        // 2. Ir à Internet converter o preço (Aula 16)
        Double precoConvertido = exchangeService.converterPreco(servico.getPreco(), moeda);

        // 3. Enviar o resultado para o Gmail do cliente (Aula 15)
        emailService.enviarOrcamentoPorEmail(emailDestino, servico.getTitulo(), precoConvertido, moeda);

        return "Orçamento calculado e enviado com sucesso para " + emailDestino + "!";
    }

    @Operation(summary = "Carregar capa do servico", description = "rota para carregar capas de servico com base no ID")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping(value = "/{id}/upload-capa", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long id
    ) {

        ServicoModel servico = servicoService.buscarServicoPorId(id);

        String fileUploaded = fileStorageService.storeImage(file);

        servico.setImagemCapa(fileUploaded);
        servicoService.criarServico(servico);
        return ResponseEntity.ok("Ficheiro carregado com sucesso:" + fileUploaded);
    }
}