package top.yzlin.beichen.keywordfunc;

import java.util.Set;

public interface KeyWordDoc {

    void setGroupId(String groupId);

    void saveKeyWord(String key,String value);

    void removeKeyWord(String key);

    boolean containsKeyWord(String key);

    String getKeyWord(String key);

    void addAdmin(String admin);

    void removeAdmin(String admin);

    int size();

    Set<String> keySet();

    boolean containsAdmin(String admin);

}
