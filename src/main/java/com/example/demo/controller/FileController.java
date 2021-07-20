package com.example.demo.controller;

import com.example.demo.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path="/file", name="file")
public class FileController {
    @Autowired
    private StorageService storageService;

    @GetMapping(value = "/", name = "list")
    public String list(Model model) throws IOException {
//        var files = storageService.loadAll().map(
//                path ->
//                        MvcUriComponentsBuilder.fromMethodName(FileController.class, "serve", path.getFileName().toString())
//                                .build()
//                                .toUri()
//                                .toString()
//        ).collect(Collectors.toList());
        List<String> files = new ArrayList<>();

        model.addAttribute("files", files);

        return "file/uploadForm";
    }

    @GetMapping(value = "/{filename:.+}", name = "serve")
    public @ResponseBody ResponseEntity<Resource> serve(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);

        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\""
        ).body(file);
    }

    @PostMapping(value = "/", name = "upload")
    public String upload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        storageService.store(file);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "You successfully uploaded " + file.getOriginalFilename() + "!"
        );

        return "redirect:/file/";
    }

//    @ExceptionHandler(StorageFileNotFoundException.class)
//    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
//        return ResponseEntity.notFound().build();
//    }
}
