package com.javanauta.transacao_api.business.services;

import com.javanauta.transacao_api.controller.dtos.TransacaoRequestDTO;
import com.javanauta.transacao_api.infrastructure.exceptions.UnprocessableEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Component
public class TransacaoService {

    private static final Logger log = LoggerFactory.getLogger(TransacaoService.class);

    private final List<TransacaoRequestDTO> listaTransacoes = new ArrayList<>();

    public void adicionaTransacao(TransacaoRequestDTO dto) {

        log.info("Iniciado o processamento de gravar transações " + dto + ".");

        if (dto.dataHora().isAfter(OffsetDateTime.now())) {
            log.error("Data e hora maiores que a data atual.");
            throw new UnprocessableEntity("Data e hora maiores que a data e horas atuais.");
        }

        if (dto.valor() < 0) {
            log.error("Valor menor que zero.");
            throw new UnprocessableEntity("Valor não pode ser menor que zero.");
        }

        listaTransacoes.add(dto);
        log.info("Transações adicionadas com sucesso.");
    }

    public void deletarTransacoes() {

        log.info("Iniciando processo de deletar transações.");

        if (listaTransacoes.isEmpty()) {
            log.error("Não existem valores a serem excluídos.");
            throw new UnprocessableEntity("Lista de transações está vazia.");
        }

        listaTransacoes.clear();
        log.info("Transações deletadas com sucesso.");
    }

    public List<TransacaoRequestDTO> buscarTransacoes(Integer intervaloBusca) {

        log.info("Iniciada a busca de transações por tempo " + intervaloBusca + ".");

        OffsetDateTime dataHoraIntervalo = OffsetDateTime.now().minusSeconds(intervaloBusca);

        log.info("Retorno de transações feito com sucesso.");
        return listaTransacoes.stream()
                .filter(transacao -> transacao.dataHora()
                        .isAfter(dataHoraIntervalo)).toList();
    }
}
