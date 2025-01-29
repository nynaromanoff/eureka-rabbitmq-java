package com.messageriamicroservice.msavaliacredito.controller;

import com.messageriamicroservice.msavaliacredito.domain.model.*;
import com.messageriamicroservice.msavaliacredito.exception.CommunicationErrorMicroservices;
import com.messageriamicroservice.msavaliacredito.exception.DadosClienteNotFoundException;
import com.messageriamicroservice.msavaliacredito.exception.ErrorSolicitacaoCartaoException;
import com.messageriamicroservice.msavaliacredito.service.AvaliadorCreditoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "OK!";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
        try{
            SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        catch (CommunicationErrorMicroservices e) {
           return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity realizarAvaliacao (@RequestBody DadosAvaliacao dados) {
        try{
           ReturnAvaliacaoCliente avaliacaoCliente = avaliadorCreditoService.returnAvaliacaoCliente(dados.getCpf(), dados.getRenda());
           return ResponseEntity.ok(avaliacaoCliente);
        } catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        catch (CommunicationErrorMicroservices e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping("solicitacao-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados) {
        try {
            ProtocoloSolicitacaoCartao  protocoloSolicitacaoCartao =
                    avaliadorCreditoService.solicitacaoEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        }catch (ErrorSolicitacaoCartaoException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
