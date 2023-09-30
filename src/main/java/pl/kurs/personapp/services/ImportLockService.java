package pl.kurs.personapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ImportLockService {

    private final RedisTemplate<String,String> redisTemplate;
    private static final String IMPORT_LOCK_KEY = "import_lock";

    @Autowired
    public ImportLockService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean tryAcquireImportLock() {
        Boolean lockAcquire = redisTemplate.opsForValue().setIfAbsent(IMPORT_LOCK_KEY, "LOCKED");
        return lockAcquire !=null && lockAcquire;
    }

    public void releaseImportLock() {
        redisTemplate.delete(IMPORT_LOCK_KEY);
    }
}
