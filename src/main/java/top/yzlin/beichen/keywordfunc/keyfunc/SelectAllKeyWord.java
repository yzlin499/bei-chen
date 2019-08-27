package top.yzlin.beichen.keywordfunc.keyfunc;

import top.yzlin.beichen.keywordfunc.KeyWord;
import top.yzlin.beichen.keywordfunc.KeyWordFunction;

public class SelectAllKeyWord implements KeyWordFunction {
    private KeyWord keyWord;

    @Override
    public void setKeyWord(KeyWord keyWord) {
        this.keyWord=keyWord;
    }

    @Override
    public boolean judgeText(String msg) {
        return "查看所有关键字".equals(msg);
    }

    @Override
    public String dealWithText(String fromQQ, String msg) {
        int ks=keyWord.size();
        if(ks==0){
            return "没有任何关键字";
        }
        StringBuilder temp=new StringBuilder(ks*2+1);
        temp.append("以下为所有关键字:");
        keyWord.keySet().forEach(key->{
            temp.append('\n');
            temp.append(key);
        });
        return temp.toString();
    }
}
