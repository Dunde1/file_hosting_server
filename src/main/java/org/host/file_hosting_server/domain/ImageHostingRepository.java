package org.host.file_hosting_server.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageHostingRepository extends JpaRepository<ImageHosting, Long> {
    //이미지 경로 검색
    Optional<ImageHosting> findByImgPath(String imgName);
}