package org.host.file_hosting_server.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.host.file_hosting_server.domain.ImageHosting;
import org.host.file_hosting_server.domain.ImageHostingRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;

@Service
@RequiredArgsConstructor
public class HostingService {
    private final Environment environment;
    private final ImageHostingRepository imageHostingRepository;

    //사용자 이미지저장 요청시 수행
    public String requestSave(String imgTitle, String requester, MultipartFile imgFile){
        //제목이 없을경우 리턴
        if(imgTitle==null) return "No_title";
        if(requester==null) return "No_requester_name";

        String fileName = imgFile.getOriginalFilename();

        //파일 이름의 확장자와 이름 나누기, 확장자는 소문자 변환.
        int pos = fileName.lastIndexOf(".");
        String extension = fileName.substring(pos+1);
        extension = extension.toLowerCase();
        String baseName = fileName.substring(0, pos);

        //지원되는 확장자가 아닐경우 리턴
        if(!("jpg".equals(extension)||"png".equals(extension)||"jpeg".equals(extension))) return "No_support_extension";

        //이곳에 파일이름 암호화코드 삽입

        //파일 저장경로 지정, 윈도우 테스트시 "C:/" 로 변경, /home/mit09/user/filename.ext
        String savePath = environment.getProperty("imgPath")+"/user/"+baseName+"."+extension;
        File file = new File("file:/"+savePath);

        //파일 저장
        try {
            imgFile.transferTo(file);
        }catch (Exception e){
            e.printStackTrace();
            return "file_conversion_error";
        }

        //데이터베이스 저장
        imageHostingRepository.save(ImageHosting.builder()
                .imgName(imgTitle)
                .imgPath(savePath)
                .build());

        return "OK";
    }

    @Transactional
    public String requestAccept(String key, Long imgNum){
        //키가 맞지 않을경우 리턴
        if(!key.equals(environment.getProperty("acceptKey"))) return "Not_matched_key";

        //데이터베이스에서 이미지주소 가져오기
        ImageHosting imageHosting = imageHostingRepository.findById(imgNum).orElseThrow(
                () -> new IllegalArgumentException("id가 없습니다."));

        //파일 이름 가져오기
        String filePath = imageHosting.getImgPath();
        int pos = filePath.lastIndexOf("/");
        String fileName = filePath.substring(pos+1);
        String newFilePath = environment.getProperty("imgPath")+"/module/"+fileName;

        //파일 경로 이동하기
        try {
            File file = new File("file:/"+filePath);
            file.renameTo(new File("file:/"+newFilePath));
        }catch (Exception e){
            e.printStackTrace();
            return "file_conversion_error";
        }

        //엔티티 수정하기
        imageHosting.setImgPath(newFilePath);

        return "OK";
    }
}
