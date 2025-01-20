package com.neo.powersearch.search.controller;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.neo.powersearch.search.service.FileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/files"})
@CrossOrigin({"http://localhost:3000"})
public class FileDownloadController {


    @Autowired
    private FileDownloadService S3DownloadService;

    @GetMapping({"/{fileName}"})
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) throws IOException {
        return S3DownloadService.downloadFile(fileName);
    }
}
