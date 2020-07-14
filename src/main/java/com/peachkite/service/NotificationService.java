package com.peachkite.service;

import com.peachkite.db.domain.Notification;
import com.peachkite.vo.ReviewVO;

public interface NotificationService {

    Notification createReviewNotification(ReviewVO reviewVO);
}
