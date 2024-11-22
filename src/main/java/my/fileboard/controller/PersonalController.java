package my.fileboard.controller;

import jakarta.websocket.server.PathParam;
import my.fileboard.dto.FileDTO;
import my.fileboard.mapper.FileMapper;
import my.fileboard.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class PersonalController {
    @Autowired
    private FileService fileService;
    @Autowired
    private FileMapper fileMapper;

    @GetMapping(path = "/personal/files")
    public String showPersonal(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<FileDTO> files = fileService.getPersonalFiles(username).stream()
                .map(fileMapper::map)
                .toList();
        model.addAttribute("files", files);
        return "personal.html";
    }

    @PostMapping(path = "/personal/upload")
    public String uploadFile(@RequestParam("file")MultipartFile file)
            throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        fileService.appendPersonalFile(username, file.getOriginalFilename(), file.getBytes());
        return "redirect:/personal/files";
    }

    @GetMapping(path = "/personal/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("name") String name, @RequestParam("id") Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        byte[] data = fileService.getPersonalData(username, id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name)
                .body(data);
    }

    @GetMapping(path = "/personal/delete")
    public String deleteFile(@RequestParam("id") Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        fileService.deletePersonalFile(username, id);
        return "redirect:/personal/files";
    }

}
