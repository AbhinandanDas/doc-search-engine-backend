package com.neo.powersearch.search.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface FileDownloadService{
    ResponseEntity<InputStreamResource> downloadFile(String fileName) throws IOException;
}
