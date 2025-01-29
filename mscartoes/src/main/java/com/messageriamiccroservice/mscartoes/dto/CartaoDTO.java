package com.messageriamiccroservice.mscartoes.dto;

import com.messageriamiccroservice.mscartoes.domain.BandeiraCartao;
import com.messageriamiccroservice.mscartoes.domain.Cartao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoDTO {
    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao toModel(){
        return new Cartao(nome, bandeira, renda, limite);
    }
}
