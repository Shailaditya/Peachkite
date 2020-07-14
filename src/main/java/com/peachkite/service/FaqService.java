package com.peachkite.service;

import com.peachkite.db.domain.Faq;
import com.peachkite.exception.FaqPublishLimitReachedException;

public interface FaqService {

    Faq createFaq(Faq faq) throws FaqPublishLimitReachedException;
    Faq updateFaq(Faq faq) throws FaqPublishLimitReachedException;
    int getMaxPublishedFaqs();

}
