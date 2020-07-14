package com.peachkite.service.impl;

import com.peachkite.db.domain.Faq;
import com.peachkite.db.repository.FaqRepository;
import com.peachkite.exception.FaqPublishLimitReachedException;
import com.peachkite.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FaqServiceImpl implements FaqService{

    @Autowired
    protected FaqRepository faqRepository;

    @Value("${company.max.published.faqs}")
    private Integer maxPublishedFaqs;

    @Override
    public Faq createFaq(Faq faq) throws FaqPublishLimitReachedException {
        int serialNumber=1;
        List<Faq> faqs=faqRepository.findByCompanyIdOrderBySerialNumber(faq.getCompanyId());
        if(Faq.Status.Published.equals(faq.getStatus()) && faqs.stream().filter(f->f.getStatus().equals(Faq.Status.Published)).collect(Collectors.toList()).size() == maxPublishedFaqs)
            throw new FaqPublishLimitReachedException("Published Faq's cannot be more than : "+maxPublishedFaqs);
        if(faqs.size() > 0){
            serialNumber=faqs.get(0).getSerialNumber()+1;
        }
        faq.setSerialNumber(serialNumber);
        faq.setIsSelected(true);
        return faqRepository.save(faq);
    }

    @Override
    public Faq updateFaq(Faq faq) throws FaqPublishLimitReachedException{
        List<Faq> faqs=faqRepository.findByCompanyIdOrderBySerialNumber(faq.getCompanyId());
        if(Faq.Status.Published.equals(faq.getStatus()) && faqs.stream().filter(f->f.getStatus().equals(Faq.Status.Published)).collect(Collectors.toList()).size() == maxPublishedFaqs)
            throw new FaqPublishLimitReachedException("Published Faq's cannot be more than : "+maxPublishedFaqs);
        return faqRepository.save(faq);
    }

    @Override
    public int getMaxPublishedFaqs(){
        return maxPublishedFaqs;
    }
}
