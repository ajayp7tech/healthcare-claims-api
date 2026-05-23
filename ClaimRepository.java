package com.ajayp.claims.repository;

import com.ajayp.claims.domain.Claim;
import com.ajayp.claims.domain.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, String> {
    List<Claim> findByMemberIdAndStatusNot(String memberId, ClaimStatus status);
}
