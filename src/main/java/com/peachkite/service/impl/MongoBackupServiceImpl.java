package com.peachkite.service.impl;

import com.peachkite.db.repository.BenefitRepository;
import com.peachkite.db.repository.CompanyRepository;
import com.peachkite.db.repository.FeedbackRepository;
import com.peachkite.db.repository.UserRepository;
import com.peachkite.service.EmailService;
import com.peachkite.service.MongoBackupService;
import com.peachkite.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class MongoBackupServiceImpl implements MongoBackupService {

    private static final Logger logger = LoggerFactory.getLogger(MongoBackupServiceImpl.class);

    @Autowired
    protected FeedbackRepository feedbackRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CompanyRepository companyRepository;

    @Autowired
    protected BenefitRepository benefitRepository;

    @Autowired
    protected EmailService emailService;

    @Value("${temp.dir}")
    protected String tempDir;

    @Scheduled(fixedDelayString = "${backup.interval.milliseconds}")
    public void backup() throws Exception {

        logger.info("MongoDB backup started");
        logger.info("Temp directory : "+tempDir);
        String zipfilePath= tempDir+"mongodb_backup.zip";

        FileOutputStream fos = new FileOutputStream(zipfilePath);
        ZipOutputStream zos = new ZipOutputStream(fos);
        //Feedback collection
        List<?> list=feedbackRepository.findAll();
        logger.info("Total feedbacks : "+list.size());
        String json= JSONUtils.convertObjectToJSON(list);
        File file=new File(tempDir+"feedback.json");
        FileCopyUtils.copy(json.getBytes(),file);
        addToZip(file.getAbsolutePath(),zos);
        file.delete();
        //User collection
        list=userRepository.findAll();
        logger.info("Total Users : "+list.size());
        json= JSONUtils.convertObjectToJSON(list);
        file=new File(tempDir+"user.json");
        FileCopyUtils.copy(json.getBytes(),file);
        addToZip(file.getAbsolutePath(),zos);
        file.delete();
        //Company collection
        list=companyRepository.findAll();
        logger.info("Total Companies : "+list.size());
        json= JSONUtils.convertObjectToJSON(list);
        file=new File(tempDir+"company.json");
        FileCopyUtils.copy(json.getBytes(),file);
        addToZip(file.getAbsolutePath(),zos);
        file.delete();
        //Benefit collection
        list=benefitRepository.findAll();
        logger.info("Total Benefits : "+list.size());
        json= JSONUtils.convertObjectToJSON(list);
        file=new File(tempDir+"benefit.json");
        FileCopyUtils.copy(json.getBytes(),file);
        addToZip(file.getAbsolutePath(),zos);
        file.delete();
        zos.close();
        fos.close();
        emailService.sendBackUpEmail(zipfilePath);
        logger.info("MongoDB backup ends");
    }

    private void addToZip(String filePath, ZipOutputStream zos)throws IOException{
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

}
