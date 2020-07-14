package com.peachkite.service;

import com.peachkite.exception.UserServiceException;
import com.peachkite.vo.ClaimCompanyVO;

public interface ClaimCompanyService {
    void processClaimRequest(ClaimCompanyVO claimCompanyVO) throws Exception;
    Boolean verifyClaimRequest(String claimId) throws Exception;
}
