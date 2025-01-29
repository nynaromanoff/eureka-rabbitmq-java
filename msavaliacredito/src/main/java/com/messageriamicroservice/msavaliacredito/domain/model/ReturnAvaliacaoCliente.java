package com.messageriamicroservice.msavaliacredito.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class ReturnAvaliacaoCliente {
    private List<CartaoAprovado> cartoes;
}
