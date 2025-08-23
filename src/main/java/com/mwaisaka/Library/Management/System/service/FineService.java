package com.mwaisaka.Library.Management.System.service;

import com.mwaisaka.Library.Management.System.domain.dto.request.PaymentRequest;
import com.mwaisaka.Library.Management.System.domain.dto.response.FineResponse;
import com.mwaisaka.Library.Management.System.domain.models.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface FineService {
    List<FineResponse> getUserFines(Long userId);
    List<FineResponse> getAllFines();
    List<FineResponse> getUnpaidFines(Long userId);
    BigDecimal getTotalUnpaidFines(Long userId);
    String processPayment(PaymentRequest paymentRequest);
    void generateFinesForOverdueBooks();
    void updateFineStatusOnReturn(Transaction returnTransaction);
}
