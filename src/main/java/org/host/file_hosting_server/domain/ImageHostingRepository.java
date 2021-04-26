package org.host.file_hosting_server.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageHostingRepository extends JpaRepository<ImageHosting, Long> {
}