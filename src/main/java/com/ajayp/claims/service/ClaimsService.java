package com.ajayp.claims.service;

import com.ajayp.claims.domain.Claim;
import com.ajayp.claims.domain.ClaimStatus;
import com.ajayp.claims.dto.ClaimRequest;
import com.ajayp.claims.dto.ClaimResponse;
import com.ajayp.claims.exception.ClaimNotFoundException;
import com.ajayp.claims.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClaimsService {

    private final ClaimRepository claimRepository;

    @Transactional
    public ClaimResponse submitClaim(ClaimRequest request) {
        Claim claim = new Claim();
        claim.setMemberId(request.memberId());
        claim.setProviderId(request.providerId());
        claim.setServiceDate(request.serviceDate());
        claim.setDiagnosisCode(request.diagnosisCode());
        claim.setProcedureCode(request.procedureCode());
        claim.setBilledAmount(request.billedAmount());
        Claim saved = claimRepository.save(claim);
        log.info("Claim submitted: {}", saved.getId());
        return toResponse(saved);
    }

    public ClaimResponse getClaim(String id) {
        return claimRepository.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new ClaimNotFoundException(id));
    }

    public List<ClaimResponse> getAllClaims() {
        return claimRepository.findAll().stream()
            .filter(c -> c.getStatus() != ClaimStatus.DELETED)
            .map(this::toResponse)
            .toList();
    }

    public List<ClaimResponse> getClaimsByMember(String memberId) {
        return claimRepository
            .findByMemberIdAndStatusNot(memberId, ClaimStatus.DELETED)
            .stream().map(this::toResponse).toList();
    }

    @Transactional
    public ClaimResponse updateStatus(String id, ClaimStatus status) {
        Claim claim = claimRepository.findById(id)
            .orElseThrow(() -> new ClaimNotFoundException(id));
        claim.setStatus(status);
        return toResponse(claimRepository.save(claim));
    }

    @Transactional
    public void deleteClaim(String id) {
        Claim claim = claimRepository.findById(id)
            .orElseThrow(() -> new ClaimNotFoundException(id));
        claim.setStatus(ClaimStatus.DELETED);
        claimRepository.save(claim);
    }

    private ClaimResponse toResponse(Claim c) {
        return new ClaimResponse(
            c.getId(), c.getMemberId(), c.getProviderId(),
            c.getServiceDate(), c.getDiagnosisCode(), c.getProcedureCode(),
            c.getBilledAmount(), c.getStatus(), c.getCreatedAt()
        );
    }
}
