package org.host.file_hosting_server.main;

import lombok.RequiredArgsConstructor;
import org.host.file_hosting_server.service.HostingService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//@CrossOrigin(origins = {"http://localhost:8888", "http://naduri.iptime.org", "http://192.168.0.13"})
@RestController
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class HostingController {
    private final HostingService hostingService;

    @PostMapping("/requestSave")
    public String requestSave(String imgTitle, Long requester, MultipartFile imgFile){
        return hostingService.requestSave(imgTitle, requester, imgFile);
    }

    @PostMapping("/requestAccept")
    public String requestAccept(String key, Long imgNum){
        return hostingService.requestAccept(key, imgNum);
    }
}
