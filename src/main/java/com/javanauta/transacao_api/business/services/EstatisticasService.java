package com.javanauta.transacao_api.business.services;

import com.javanauta.transacao_api.controller.dtos.EstatisticaResponseDTO;
import com.javanauta.transacao_api.controller.dtos.TransacaoRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstatisticasService {

    private static final Logger log = LoggerFactory.getLogger(EstatisticasService.class);

    @Autowired
    public TransacaoService transacaoService;

    public EstatisticaResponseDTO calcularEstatisticasTransacoes(Integer intervaloBusca) {

        log.info("Iniciada a busca de estatisticas de transações pelo período de tempo " + intervaloBusca + " segundos.");

        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(intervaloBusca);

        if (transacoes.isEmpty()) {
            return new EstatisticaResponseDTO(0L, 0.0, 0.0, 0.0, 0.0);
        }

        DoubleSummaryStatistics estatisticasTransacoes = transacoes.stream()
                .mapToDouble(TransacaoRequestDTO::valor)
                .summaryStatistics();

        log.info("Retorno de estatisticas realizada com sucesso.");

        return new EstatisticaResponseDTO(estatisticasTransacoes.getCount(),
                estatisticasTransacoes.getSum(),
                estatisticasTransacoes.getAverage(),
                estatisticasTransacoes.getMin(),
                estatisticasTransacoes.getMax());
    }

}
