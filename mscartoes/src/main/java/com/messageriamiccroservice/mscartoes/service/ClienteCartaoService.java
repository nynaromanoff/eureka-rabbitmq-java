package com.messageriamiccroservice.mscartoes.service;

import com.messageriamiccroservice.mscartoes.domain.ClienteCartao;
import com.messageriamiccroservice.mscartoes.repositores.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository repository;

    public List<ClienteCartao> listCartaoCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}
