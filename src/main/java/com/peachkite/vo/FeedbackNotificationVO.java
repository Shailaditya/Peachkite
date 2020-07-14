package com.peachkite.vo;

import com.peachkite.db.domain.Company;
import com.peachkite.db.domain.Feedback;
import lombok.Data;
import java.util.List;

@Data
public class FeedbackNotificationVO {

    private String comment;
    private List<Company.CompanyBenefit> benefits;

    public FeedbackNotificationVO(Feedback feedback){
        this.comment=feedback.getComment();
        this.benefits=feedback.getBenefits();
    }
}
