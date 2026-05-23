package com.ajayp.claims.controller;

import com.ajayp.claims.domain.ClaimStatus;
import com.ajayp.claims.dto.ClaimRequest;
import com.ajayp.claims.dto.ClaimResponse;
import com.ajayp.claims.service.ClaimsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
public class ClaimsController {

    private final ClaimsService claimsService;

    @PostMapping
    public ResponseEntity<ClaimResponse> submitClaim(@Valid @RequestBody ClaimRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(claimsService.submitClaim(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponse> getClaim(@PathVariable String id) {
        return ResponseEntity.ok(claimsService.getClaim(id));
    }

    @GetMapping
    public ResponseEntity<List<ClaimResponse>> getAllClaims() {
        return ResponseEntity.ok(claimsService.getAllClaims());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByMember(@PathVariable String memberId) {
        return ResponseEntity.ok(claimsService.getClaimsByMember(memberId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ClaimResponse> updateStatus(
            @PathVariable String id,
            @RequestParam ClaimStatus status) {
        return ResponseEntity.ok(claimsService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable String id) {
        claimsService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }
}
