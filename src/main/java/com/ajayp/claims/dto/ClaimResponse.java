package com.ajayp.claims.dto;

import com.ajayp.claims.domain.ClaimStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClaimResponse(
    String id,
    String memberId,
    String providerId,
    LocalDate serviceDate,
    String diagnosisCode,
    String procedureCode,
    BigDecimal billedAmount,
    ClaimStatus status,
    LocalDateTime createdAt
) {}
