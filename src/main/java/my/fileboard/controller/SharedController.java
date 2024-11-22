package my.fileboard.controller;

import jakarta.websocket.server.PathParam;
import my.fileboard.dto.FileDTO;
import my.fileboard.mapper.FileMapper;
import my.fileboard.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SharedController {

    @Autowired
    private FileService fileService;
    @Autowired
    private FileMapper fileMapper;

    @GetMapping(path = "/shared/files")
    public String showShared(Model model) {
        List<FileDTO> files = fileService.getSharedFiles().stream()
                .map(fileMapper::map)
                .toList();
        model.addAttribute("files", files);
        return "shared.html";
    }

    @GetMapping(path = "/shared/download")
    public ResponseEntity<byte[]> downloadShared(@RequestParam("name") String name, @RequestParam("id") Long id) {
        byte[] data = fileService.getSharedData(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name)
                .body(data);
    }
}
