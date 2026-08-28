package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.exception.ServicoNaoEncontradoException;
import com.labanta.servidorlocal.model.ServicoModel;
import com.labanta.servidorlocal.repository.ServicoRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository repositorio;

    public ServicoService(
            ServicoRepository repositorio) {

        this.repositorio = repositorio;
    }

    public Page<ServicoModel> listarTodos(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    public ServicoModel criarServico(ServicoModel servico) {
        return repositorio.save(servico);
    }

    public ServicoModel buscarServicoPorId(Long id) {

        return repositorio.findById(id)
                .orElseThrow(() ->
                        new ServicoNaoEncontradoException(
                                "O serviço com o ID " + id +
                                        " não existe no catálogo."
                        )
                );
    }

    public List<ServicoModel> aplicarDescontoEmAtivos(
            Double percentagem) {

        if (percentagem < 0
                || percentagem > 100) {

            throw new IllegalArgumentException(
                    "Desconto inválido."
            );
        }

        List<ServicoModel> lista =
                repositorio.findByEstaAtivoTrue();

        for (ServicoModel servico : lista) {

            Double precoAtual =
                    servico.getPreco();

            Double desconto =
                    precoAtual * percentagem / 100;

            Double precoComDesconto =
                    precoAtual - desconto;

            servico.setPrecoComDesconto(
                    precoComDesconto
            );
        }

        repositorio.saveAll(lista);

        return lista;
    }

    public List<ServicoModel> pesquisarPorTitulo(
            String termo) {

        return repositorio
                .findByTituloContainingIgnoreCase(termo);
    }
}