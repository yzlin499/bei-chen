package top.yzlin.beichen.keywordfunc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class KeyWordDocSaveRedis implements KeyWordDoc , InitializingBean {
    private String groupId;
    private RedisTemplate<String,String> redisTemplate;
    private String keyWordHash;
    private String keyWordAdminSet;

    @Override
    public void afterPropertiesSet() throws Exception {
        keyWordHash="cqrobot.keyword."+groupId+".keyWordHash";
        keyWordAdminSet="cqrobot.keyword."+groupId+".keyWordAdminSet";
    }

    @Override
    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId=groupId;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveKeyWord(String key, String value) {
        redisTemplate.opsForHash().put(keyWordHash,key,value);
    }

    @Override
    public String getKeyWord(String key) {
        return redisTemplate.opsForHash().get(keyWordHash,key).toString();
    }

    @Override
    public void removeKeyWord(String key) {
        redisTemplate.opsForHash().delete(keyWordHash,key);
    }

    @Override
    public boolean containsKeyWord(String key) {
        return redisTemplate.opsForHash().hasKey(keyWordHash,key);
    }

    @Override
    public void addAdmin(String admin) {
        redisTemplate.opsForSet().add(keyWordAdminSet,admin);
    }

    @Override
    public void removeAdmin(String admin) {
        redisTemplate.opsForSet().remove(keyWordAdminSet,admin);
    }

    @Override
    public int size() {
        return Math.toIntExact(redisTemplate.opsForHash().size(keyWordHash));
    }

    @Override
    public Set<String> keySet() {
        return redisTemplate.opsForHash().keys(keyWordHash).stream().map(Object::toString).collect(Collectors.toSet());
    }

    @Override
    public boolean containsAdmin(String admin) {
        return Optional.ofNullable(redisTemplate.opsForSet().isMember(keyWordAdminSet,admin)).orElse(false);
    }
}
