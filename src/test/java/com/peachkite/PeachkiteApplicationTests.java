package com.peachkite;

import com.peachkite.db.domain.Benefit;
import com.peachkite.db.domain.Company;
import com.peachkite.db.repository.BenefitRepository;
import com.peachkite.db.repository.CompanyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PeachkiteApplicationTests {

	@Autowired
	BenefitRepository benefitRepo;

	@Autowired
	CompanyRepository companyRepository;


	@Test
	public void contextLoads() {
		String[] labels={"Ratio of women in company",

				"Equal Opportunities & Pay for Men and Women",

				"Women are part of Top management",

				"Management opportunities for Women",

				"Hiring program for Women on a break",

				"Maternity & Adoption support & benefits",

				"Phase back program (ease back to work post maternity/break)",

				"Medical insurance - women and childcare",

				"Day care services - internal & sponsored",

				"Wellness initiatives & care (Employee & Family)",

				"Flexible work hours",

				"Ability to telecommute/work remotely",

				"Mobility / Relocation programs",

				"Learning Opportunities (Internal / External trainings)",

				"Higher education support",

				"Safe work environment",

				"Workplace commute support",

				"Zero tolerance on Sexual Harassment",

				"Supportive culture & peer group"

		};
		benefitRepo.deleteAll();
		List<Benefit> benefits = new ArrayList<>();
		for(String label : labels){
			Benefit benefit = new Benefit();
			benefit.setLabel(label);
			benefits.add(benefit);
		}
		benefits=benefitRepo.save(benefits);
		List<Company> companies = companyRepository.findAll();
		for(Company company:companies){
			company.setBenefits(convertToCompanBenefit(benefits));
		}
		companyRepository.save(companies);
	}

	private List<Company.CompanyBenefit> convertToCompanBenefit(List<Benefit> benefits){
		List<Company.CompanyBenefit> companyBanafits = new ArrayList<>();
		for(Benefit benefit : benefits){
			Company.CompanyBenefit cb=new Company.CompanyBenefit();
			cb.setId(benefit.getId());
			cb.setRating(0.0d);
			cb.setLabel(benefit.getLabel());
			companyBanafits.add(cb);
		}
		return companyBanafits;
	}
}
