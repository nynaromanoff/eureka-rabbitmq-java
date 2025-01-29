package com.messageriamiccroservice.mscartoes.controllers;

import com.messageriamiccroservice.mscartoes.domain.Cartao;
import com.messageriamiccroservice.mscartoes.domain.ClienteCartao;
import com.messageriamiccroservice.mscartoes.dto.CartaoDTO;
import com.messageriamiccroservice.mscartoes.dto.CartoesPorClienteDTO;
import com.messageriamiccroservice.mscartoes.service.CartaoService;
import com.messageriamiccroservice.mscartoes.service.ClienteCartaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
@RequiredArgsConstructor
public class CartoesResource {

    private final CartaoService cartaoService;
    private final ClienteCartaoService clienteCartaoService;

   @GetMapping
    public String status() {
        return "Ok";
    }

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody CartaoDTO request){
        Cartao cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAteh(@RequestParam("renda") Long renda) {
       List<Cartao> list = cartaoService.getCartoesRendaMenorIgual(renda);
       return ResponseEntity.ok(list);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteDTO>> getCartoesByClinte(@RequestParam("cpf") String cpf) {
       List<ClienteCartao> list = clienteCartaoService.listCartaoCpf(cpf);
       List<CartoesPorClienteDTO> resultList = list.stream()
               .map(CartoesPorClienteDTO::fromModel)
               .collect(Collectors.toList());
       return ResponseEntity.ok(resultList);
    }
}
