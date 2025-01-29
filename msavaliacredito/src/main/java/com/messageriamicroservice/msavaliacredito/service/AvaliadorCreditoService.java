package com.messageriamicroservice.msavaliacredito.service;

import com.messageriamicroservice.msavaliacredito.domain.model.*;
import com.messageriamicroservice.msavaliacredito.exception.CommunicationErrorMicroservices;
import com.messageriamicroservice.msavaliacredito.exception.DadosClienteNotFoundException;
import com.messageriamicroservice.msavaliacredito.exception.ErrorSolicitacaoCartaoException;
import com.messageriamicroservice.msavaliacredito.infra.client.CartoesResourceClient;
import com.messageriamicroservice.msavaliacredito.infra.client.ClienteResourceClient;
import com.messageriamicroservice.msavaliacredito.infra.queue.SolicitacaoEmissaoCartaoPublisher;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService{

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesResourceClient;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;
    public SituacaoCliente obterSituacaoCliente(String cpf)
            throws DadosClienteNotFoundException, CommunicationErrorMicroservices{
        //obter dados do clientes - MSClientes
        //obter dados cartoes - MSCartoes
        try {
            ResponseEntity<DadosCliente> responseEntity = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartaosResponse = cartoesResourceClient.getCartoesByClinte(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(responseEntity.getBody())
                    .cartoes(cartaosResponse.getBody())
                    .build();
        }catch (FeignException.FeignClientException e) {
           int status = e.status();
           if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
           }
           throw new CommunicationErrorMicroservices(e.getMessage(), status);
        }
    }

    public ReturnAvaliacaoCliente returnAvaliacaoCliente(String cpf, Long renda)
            throws DadosClienteNotFoundException, CommunicationErrorMicroservices{
        try {
            ResponseEntity<DadosCliente> clienteResponse = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartaoResponse = cartoesResourceClient.getCartoesRendaAteh(renda);

            List<Cartao> cartoes =  cartaoResponse.getBody();
            var listaCartoesAprovador = cartoes.stream().map(cartao -> {
                DadosCliente dadosCliente = clienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal rendaBasica = BigDecimal.valueOf(renda);
                BigDecimal idade = BigDecimal.valueOf(dadosCliente.getIdade());

                var fator = idade.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).collect(Collectors.toList());

            return new ReturnAvaliacaoCliente(listaCartoesAprovador);

        }catch (FeignException.FeignClientException e) {
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new CommunicationErrorMicroservices(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitacaoEmissaoCartao(DadosSolicitacaoEmissaoCartao dados){
        try{
            emissaoCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        }catch (Exception e) {
            throw new ErrorSolicitacaoCartaoException(e.getMessage());
        }
    }
}
