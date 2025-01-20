package com.neo.powersearch.search.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.neo.powersearch.search.service.DocumentService;
import com.neo.powersearch.search.service.FileUploadService;
import com.neo.powersearch.search.service.NewInvertedIndexService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/api/files"})
@CrossOrigin({"http://localhost:3000"})
public class FileUploadController {
    private static final String UPLOAD_DIR = "uploaded_files/";
    @Autowired
    private DocumentService documentService;
    @Autowired
    private NewInvertedIndexService indexService;

    @Autowired
    private FileUploadService s3Service;
    @PostMapping({"/upload"})
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        } else {
            try {
                String docId = UUID.randomUUID().toString();
                String fileName = file.getOriginalFilename();
                String extractedText = this.extractTextFromFile(file);

                String result = s3Service.uploadFile(file);


                /* Old code to store in local*/
//                Path filePath = Paths.get("uploaded_files/", fileName);
//                Files.createDirectories(filePath.getParent());
//                Files.write(filePath, file.getBytes(), new OpenOption[0]);

                this.documentService.saveDocument(docId, extractedText, fileName, file.getContentType());
                this.indexService.indexDocument(docId);
                return ResponseEntity.ok(result);
            } catch (Exception var6) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File processing failed");
            }
        }
    }

    String extractTextFromFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(".txt")) {
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } else if (fileName.endsWith(".pdf")) {
            return this.extractTextFromPdf(file);
        } else {
            throw new IllegalArgumentException("Unsupported File Format");
        }
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        PDDocument document = Loader.loadPDF(file.getBytes());

        String var4;
        try {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            var4 = pdfStripper.getText(document);
        } catch (Throwable var6) {
            if (document != null) {
                try {
                    document.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }

            throw var6;
        }

        if (document != null) {
            document.close();
        }

        return var4;
    }
}
