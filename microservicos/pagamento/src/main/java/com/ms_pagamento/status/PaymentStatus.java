package com.ms_pagamento.status;

public enum PaymentStatus {
    PENDING, //aguarda processamento
    PROCESSING, // comunicação com gateway
    APROVED, //pagamento aprovado
    DECLINED, //pagamento recusado
    CANCELED, //cancelado
    REFUNDED;//estornado
}
