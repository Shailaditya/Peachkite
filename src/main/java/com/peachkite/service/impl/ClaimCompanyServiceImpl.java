package com.peachkite.service.impl;

import com.peachkite.db.domain.Company;
import com.peachkite.db.domain.CompanyClaimRequest;
import com.peachkite.db.domain.User;
import com.peachkite.db.repository.CompanyClaimRequestRepository;
import com.peachkite.exception.UserServiceException;
import com.peachkite.service.ClaimCompanyService;
import com.peachkite.service.CompanyService;
import com.peachkite.service.EmailService;
import com.peachkite.service.UserService;
import com.peachkite.vo.ClaimCompanyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Date;

@Service
public class ClaimCompanyServiceImpl implements ClaimCompanyService {

    @Autowired
    protected CompanyClaimRequestRepository claimRequestRepository;

    @Autowired
    protected UserService userService;

    @Autowired
    protected CompanyService companyService;

    @Autowired
    protected EmailService emailService;

    @Value("${mail.company-claim.token.expiry}")
    protected Integer tokenExpiry;

    @Override
    public void processClaimRequest(ClaimCompanyVO claimCompanyVO) throws Exception {
        CompanyClaimRequest claimRequest=new CompanyClaimRequest();
        claimRequest.setCompanyId(claimCompanyVO.getCompanyId());
        claimRequest.setCompanName(claimCompanyVO.getCompanyName());
        claimRequest.setUserEmail(claimCompanyVO.getEmail());
        claimRequest.setCreatedAt(new Date());
        claimRequest=claimRequestRepository.save(claimRequest);
        emailService.sendClaimCompanyApprovalMail(claimCompanyVO.getHostname(),claimRequest);
        emailService.sendCompanyClaimMailReceivedToUser(claimCompanyVO.getEmail(),claimCompanyVO.getCompanyName());
    }

    @Override
    public Boolean verifyClaimRequest(String claimId) throws Exception {
        CompanyClaimRequest claimRequest=claimRequestRepository.findOne(claimId);
        if(claimRequest == null){
            return false;
        }else if( claimRequest.getCreatedAt() == null ||
                (claimRequest.getCreatedAt().getTime() + (tokenExpiry*60*60*1000)) < new Date().getTime()){
            return false;
        }else{
            User user=userService.getUserByEmail(claimRequest.getUserEmail());
            if(user == null)
                user=new User();
            user.setCompanyId(claimRequest.getCompanyId());
            user.setRoles(Arrays.asList(User.ROLE_COMPANY_ADMIN));
            if(user.getId()!=null)
                user=userService.update(user);
            else
                user=userService.create(user);
            Company company=companyService.findCompanyById(claimRequest.getCompanyId());
            company.setAdminUserId(user.getId());
            company.setIsVerified(true);
            companyService.save(company);
            claimRequest.setIsApproved(true);
            claimRequestRepository.save(claimRequest);
            emailService.sendCompanyClaimAcceptedMailToUser(claimRequest.getUserEmail(),claimRequest.getCompanName());
            return true;
        }

    }
}
