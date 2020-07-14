package com.peachkite;

import com.peachkite.db.domain.Benefit;
import com.peachkite.db.domain.Company;
import com.peachkite.db.domain.Faq;
import com.peachkite.db.domain.MultimediaContent;
import com.peachkite.db.repository.BenefitRepository;
import com.peachkite.db.repository.CompanyRepository;
import com.peachkite.db.repository.FaqRepository;
import com.peachkite.db.repository.MultimediaContentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenerateData {

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FaqRepository faqRepository;

    @Autowired
    private MultimediaContentRepository multimediaContentRepository;

//    @Test
    public void syncBenefits(){
        /*List<Benefit> benefitList=benefitRepository.findAll();

        List<Company> companyList=companyRepository.findAll();

        for(Company company:companyList){

            Map<String,Company.CompanyBenefit> companyBenefits = new HashMap<>();
            for(Company.CompanyBenefit companyBenefit:company.getBenefits())
                companyBenefits.put(companyBenefit.getId(),companyBenefit);

            for(Benefit benefit:benefitList){
                Company.CompanyBenefit cbf=companyBenefits.get(benefit.getId());
                if(cbf == null){
                    cbf=new Company.CompanyBenefit();
                    cbf.setId(benefit.getId());
                    cbf.setLabel(benefit.getLabel());
                    cbf.setIsSelected(true);
                    cbf.setRating(0.0D);
                    cbf.setCategory(benefit.getCategory());
                    cbf.setType(Company.CompanyBenefit.TYPE_STANDARD);
                    company.getBenefits().add(cbf);
                }else{
                    cbf.setLabel(benefit.getLabel());
                    cbf.setCategory(benefit.getCategory());
                    cbf.setType(Company.CompanyBenefit.TYPE_STANDARD);
                }
            }

        }
        companyRepository.save(companyList);*/

    }

//    @Test
    public void generateFaqs(){
        /*List<Faq> faqs=new ArrayList<>();
        for(int i=0;i<8;i++){
            Faq faq=new Faq();
            faq.setCompanyId("59e727426262f5099c746acc");
            faq.setIsSelected(true);
            faq.setQuestion("Question "+i);
            faq.setResponse("Answer "+i);
            faq.setSerialNumber(i);
            faq.setStatus(Faq.Status.Published);
            faqs.add(faq);
        }
        this.faqRepository.save(faqs);*/
    }

    /*@Test
    public void generateContent(){
        List<MultimediaContent> contentList=new ArrayList<>();
        for(int i=0;i<16;i++){
            MultimediaContent content=new MultimediaContent();
            content.setCompanyId("59e727426262f5099c746acc");
            content.setStartDate(null);
            content.setEndDate(null);
            content.setStatus(MultimediaContent.Status.ACTIVE);
            content.setType(i%2==0?MultimediaContent.Type.YOUTUBE_VIDEO:MultimediaContent.Type.LINK);
            content.setThumbnail(i%2==0?"http://img.youtube.com/vi/f-UzOpuKOVY/maxresdefault.jpg":"http://placehold.it/360x240");
            content.setUrl(i%2==0?"https://www.youtube.com/watch?v=f-UzOpuKOVY":"https://www.thewebtaylor.com/articles/how-to-get-a-youtube-videos-thumbnail-image-in-high-quality");
            content.setTitle("Test Data");
            contentList.add(content);
        }
        this.multimediaContentRepository.save(contentList);
    }*/
}
