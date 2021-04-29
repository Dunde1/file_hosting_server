package org.host.file_hosting_server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ImageHosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgNum;

    //유저넘버(외래키 X)
    @Column(nullable = false)
    private Long userInfoId;

    //이미지 이름(제목)
    @Column(nullable = false)
    private String imgName;

    //이미지 저장 경로
    @Column(nullable = false, unique = true)
    private String imgPath;

    @Builder
    public ImageHosting(Long userInfoId, String imgName, String imgPath){
        this.userInfoId = userInfoId;
        this.imgName = imgName;
        this.imgPath = imgPath;
    }
}
