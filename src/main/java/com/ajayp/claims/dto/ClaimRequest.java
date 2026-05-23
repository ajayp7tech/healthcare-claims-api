package com.ajayp.claims.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ClaimRequest(
    @NotBlank String memberId,
    @NotBlank String providerId,
    @NotNull LocalDate serviceDate,
    String diagnosisCode,
    String procedureCode,
    @NotNull @Positive BigDecimal billedAmount
) {}
