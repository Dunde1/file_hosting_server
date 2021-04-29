package org.host.file_hosting_server.service;

import lombok.RequiredArgsConstructor;
import org.host.file_hosting_server.domain.ImageHosting;
import org.host.file_hosting_server.domain.ImageHostingRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HostingService {
    private final Environment environment;
    private final ImageHostingRepository imageHostingRepository;

    //사용자 이미지저장 요청시 수행
    public String requestSave(String imgTitle, Long requester, MultipartFile imgFile){
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

        //파일이름 암호화코드
        baseName = getSHA256(imgTitle + requester + baseName);

        //파일 저장경로 지정, 윈도우 테스트시 "C:/" 로 변경, /home/mit09/user/{filename}.{extension}
        String savePath = "user/"+baseName+"."+extension;
        String filePath = environment.getProperty("imgPath")+"/"+savePath;


        //데이터베이스 중복확인
        if(imageHostingRepository.findByImgPath(savePath).isPresent()) return "No_redundant_storage";

        String pathPrefix = environment.getProperty("prefix");
        File file = new File(pathPrefix+filePath);

        //파일 저장
        try {
            imgFile.transferTo(file);
        }catch (Exception e){
            e.printStackTrace();
            return "file_conversion_error";
        }

        //데이터베이스 저장 및 이미지 번호 확인
        Long imgNum = imageHostingRepository.save(ImageHosting.builder()
                .userInfoId(requester)
                .imgName(imgTitle)
                .imgPath(savePath)
                .build()).getImgNum();

        //이미지 키넘버 리턴
        return Long.toString(imgNum);
    }

    //관리자 승인 메소드
    @Transactional
    public String requestAccept(String key, Long imgNum){
        //키가 맞지 않을경우 리턴
        if(!key.equals(environment.getProperty("acceptKey"))) return "Not_matched_key";

        //데이터베이스에서 이미지주소 가져오기
        Optional<ImageHosting> optionalImageHosting = imageHostingRepository.findById(imgNum);

        //id가 없을경우 리턴
        if(!optionalImageHosting.isPresent()) return "No_image_id";

        //파일 이름 가져오기
        ImageHosting imageHosting = optionalImageHosting.get();
        String filePath = imageHosting.getImgPath();
        int pos = filePath.lastIndexOf("/");
        String fileName = filePath.substring(pos+1);
        String newFilePath = "module/"+fileName;

        if(filePath.equals(newFilePath)) return "Redundant_work";

        //파일경로 접두어
        String pathPrefix = environment.getProperty("prefix")+environment.getProperty("imgPath")+"/";

        //파일 경로 이동하기
        try {
            File file = new File(pathPrefix+filePath);
            file.renameTo(new File(pathPrefix+newFilePath));
        }catch (Exception e){
            e.printStackTrace();
            return "file_conversion_error";
        }

        //엔티티 수정하기
        imageHosting.setImgPath(newFilePath);

        return "OK";
    }

    //문자열 SHA-256 암호화
    private String getSHA256(String input){
        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return toReturn;
    }
}
