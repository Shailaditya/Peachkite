package com.peachkite.rest.controller.companyadmin;

import com.peachkite.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/upload")
public class FileUploadController {

    @Value("${company.files.upload.location}")
    protected String companyFilesUploadLocation;

    @Value("${company.files.url.prefix}")
    protected String companyFilesUrlPrefix;

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @RequestMapping(value="",method = RequestMethod.POST)
    ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,@RequestParam("validate_dim") String allowedDimesnion){
        String filePath=companyFilesUploadLocation+file.getOriginalFilename();
        logger.info("File path : "+filePath);
        Path path = Paths.get(filePath);
        try{
            java.nio.file.Files.write(path, file.getBytes());
            String dim=ImageUtils.getImageDimension(filePath);
            if(!dim.equals(allowedDimesnion)){
                new File(filePath).delete();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (IOException e){
            logger.error("Error uploading file",e);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map<String,String> response=new HashMap<>();
        logger.info("File url : "+companyFilesUrlPrefix+file.getOriginalFilename());
        response.put("url",companyFilesUrlPrefix+file.getOriginalFilename());
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }


}
