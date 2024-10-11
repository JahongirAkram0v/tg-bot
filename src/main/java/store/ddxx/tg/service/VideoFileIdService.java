package store.ddxx.tg.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.ddxx.tg.model.VideoFileId;
import store.ddxx.tg.repository.VideoFileIdRepo;

@Service
@RequiredArgsConstructor
public class VideoFileIdService {

    private final VideoFileIdRepo repo;

    public void save(VideoFileId fileId) {
        repo.save(fileId);
    }

    public VideoFileId find() {
        return repo.find().orElse(null);
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}
