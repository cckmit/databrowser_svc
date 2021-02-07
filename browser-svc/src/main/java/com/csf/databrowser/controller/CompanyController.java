package com.csf.databrowser.controller;

import com.csf.databrowser.request.ExtractRequest;
import com.csf.databrowser.resp.CompaniesInfoResp;
import com.csf.databrowser.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Resource
    private CompanyService companyService;

    @GetMapping("")
    public CompaniesInfoResp getCompanyInfo(String keyword,
                                            @RequestParam(name = "page", defaultValue = "0") Integer page,
                                            @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return companyService.getCompanyInfo(keyword, page, size);
    }

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) throws IOException {
        if(file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(companyService.searchCompanys(file.getInputStream()));
    }

    @PostMapping("/extract")
    public ResponseEntity extract(@RequestBody ExtractRequest request){
        return ResponseEntity.ok(companyService.extract(request));
    }

    @GetMapping("/download")
    public ResponseEntity download(HttpServletRequest request) {

        return ResponseEntity.ok(companyService.download(request));
    }

}
