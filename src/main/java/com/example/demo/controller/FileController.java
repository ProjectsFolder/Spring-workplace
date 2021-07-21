package com.example.demo.controller;

import com.example.demo.dto.FileDto;
import com.example.demo.exception.StorageFileNotFoundException;
import com.example.demo.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
@Secured("ROLE_USER")
@RequestMapping(path="/file", name="file")
public class FileController {
    private static final String FILE_THREAD = "global";

    @Autowired
    private StorageService storageService;

    @GetMapping(value = "/", name = "list")
    public String list(Model model) {
        model.addAttribute("files", storageService.loadAll());
        model.addAttribute("fileForm", new FileDto());

        return "file/uploadForm";
    }

    @GetMapping(value = "/{id}", name = "serve")
    public @ResponseBody ResponseEntity<Resource> serve(@PathVariable String id)
            throws StorageFileNotFoundException {

        var storedFile = storageService.load(id);
        var file = new File(storedFile.getFilePath());
        if (file.exists()) {
            return ResponseEntity.ok().header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + storedFile.getFileName() + "\""
            ).body(new FileSystemResource(file));
        }

        throw new StorageFileNotFoundException();
    }

    @PostMapping(value = "/", name = "upload")
    public String upload(
            @ModelAttribute("fileForm") @Validated FileDto fileForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("files", storageService.loadAll());

            return "file/uploadForm";
        }

        var file = fileForm.getFile();
        try {
            storageService.store(FILE_THREAD, file);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "You successfully uploaded <b>" + file.getOriginalFilename() + "</b>"
            );
        } catch (IOException exception) {
            redirectAttributes.addFlashAttribute("dangerMessage", exception.getMessage());
        }

        return "redirect:/file/";
    }

    @DeleteMapping(value = "/{id}", name = "delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes)
            throws StorageFileNotFoundException {

        storageService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "You successfully deleted file");

        return "redirect:/file/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }
}
