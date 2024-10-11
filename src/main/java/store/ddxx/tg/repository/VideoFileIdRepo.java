package store.ddxx.tg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import store.ddxx.tg.model.VideoFileId;

import java.util.Optional;

public interface VideoFileIdRepo extends JpaRepository<VideoFileId, String> {

    @Query("SELECT fileId FROM VideoFileId")
    Optional<VideoFileId> find();
}
