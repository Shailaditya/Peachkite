package com.peachkite.service;

import java.net.UnknownHostException;
import java.util.List;

import com.peachkite.db.domain.Company;
import com.peachkite.db.domain.MissingCompany;
import com.peachkite.vo.SearchCompanyVO;

import javax.mail.MessagingException;

public interface CompanyService {
	void addToIndex(SearchCompanyVO companyVO) throws UnknownHostException;
	List<SearchCompanyVO> searchCompanyName(String searchString) throws UnknownHostException;
	Company findCompanyById(String id);
	void incrementFeedbackCount(String companyId,int count);
	void createQuery(String companyId,String query)throws MessagingException;
	Company save(Company company);
	List<Company.CompanyBenefit> saveBenefits(List<Company.CompanyBenefit> benefits,String companyId);
	MissingCompany saveMissingCompany(MissingCompany company);
	void updateRating(String companyId);
	void viewCompany(String companyId);
}
