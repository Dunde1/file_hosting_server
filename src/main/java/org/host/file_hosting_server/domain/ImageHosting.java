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

    @Column(nullable = false)
    private String imgName;

    @Column(nullable = false)
    private String imgPath;

    @Builder
    public ImageHosting(String imgName, String imgPath){
        this.imgName = imgName;
        this.imgPath = imgPath;
    }
}
