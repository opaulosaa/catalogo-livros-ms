package com.ms_pagamento.pagamento.repository;

import com.ms_pagamento.pagamento.model.PagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<PagamentoModel, Long> {
}
