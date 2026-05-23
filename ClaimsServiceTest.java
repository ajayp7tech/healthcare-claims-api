package com.ajayp.claims.service;

import com.ajayp.claims.domain.Claim;
import com.ajayp.claims.domain.ClaimStatus;
import com.ajayp.claims.dto.ClaimRequest;
import com.ajayp.claims.dto.ClaimResponse;
import com.ajayp.claims.exception.ClaimNotFoundException;
import com.ajayp.claims.repository.ClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimsServiceTest {

    @Mock
    private ClaimRepository claimRepository;

    @InjectMocks
    private ClaimsService claimsService;

    private Claim testClaim;

    @BeforeEach
    void setUp() {
        testClaim = new Claim();
        testClaim.setId("CLM-001");
        testClaim.setMemberId("MBR-100");
        testClaim.setProviderId("PRV-200");
        testClaim.setServiceDate(LocalDate.now());
        testClaim.setBilledAmount(new BigDecimal("250.00"));
        testClaim.setStatus(ClaimStatus.SUBMITTED);
    }

    @Test
    void submitClaim_shouldReturnClaimResponse() {
        when(claimRepository.save(any(Claim.class))).thenReturn(testClaim);
        ClaimRequest request = new ClaimRequest(
            "MBR-100", "PRV-200", LocalDate.now(),
            "Z00.00", "99213", new BigDecimal("250.00")
        );
        ClaimResponse response = claimsService.submitClaim(request);
        assertThat(response).isNotNull();
        assertThat(response.memberId()).isEqualTo("MBR-100");
        assertThat(response.status()).isEqualTo(ClaimStatus.SUBMITTED);
        verify(claimRepository, times(1)).save(any(Claim.class));
    }

    @Test
    void getClaim_shouldReturnClaimWhenFound() {
        when(claimRepository.findById("CLM-001")).thenReturn(Optional.of(testClaim));
        ClaimResponse response = claimsService.getClaim("CLM-001");
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo("CLM-001");
    }

    @Test
    void getClaim_shouldThrowWhenNotFound() {
        when(claimRepository.findById("UNKNOWN")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> claimsService.getClaim("UNKNOWN"))
            .isInstanceOf(ClaimNotFoundException.class);
    }

    @Test
    void deleteClaim_shouldSetStatusToDeleted() {
        when(claimRepository.findById("CLM-001")).thenReturn(Optional.of(testClaim));
        when(claimRepository.save(any(Claim.class))).thenReturn(testClaim);
        claimsService.deleteClaim("CLM-001");
        verify(claimRepository).save(argThat(c -> c.getStatus() == ClaimStatus.DELETED));
    }
}
