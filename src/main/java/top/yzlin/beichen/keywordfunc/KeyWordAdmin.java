package top.yzlin.beichen.keywordfunc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.yzlin.beichen.keywordfunc.keyfunc.*;
import top.yzlin.cqrobotsdk.CQRobot;
import top.yzlin.cqrobotsdk.cqinfo.PersonMsgInfo;
import top.yzlin.cqrobotsdk.msginterface.PersonMsgSolution;

@Component
public class KeyWordAdmin implements PersonMsgSolution, InitializingBean {
    private KeyWord keyWord;
    private KeyWordFunction[] keyWordFunctions;
    private CQRobot cqRobot;

    @Override
    public void afterPropertiesSet() throws Exception {
        keyWordFunctions=new KeyWordFunction[]{
                new AddKeyWord(),
                new AddAdmin(),
                new RemoveAdmin(),
                new RemoveKeyWord(),
                new SelectAllKeyWord(),
                new SelectKeyWord(),
                new KeyWordHelp()
        };
        for (KeyWordFunction keyWordFunction : keyWordFunctions) {
            keyWordFunction.setKeyWord(keyWord);
        }
    }

    @Autowired
    @Lazy
    public void setCqRobot(CQRobot cqRobot) {
        this.cqRobot = cqRobot;
    }

    @Autowired
    public void setKeyWord(KeyWord keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public void msgSolution(PersonMsgInfo personMsgInfo) {
        if(keyWord.containsAdmin(personMsgInfo.getFromQQ())){
            for (KeyWordFunction keyWordFunction : keyWordFunctions) {
                if (keyWordFunction.judgeText(personMsgInfo.getMsg())) {
                    cqRobot.sendPersonMsg(personMsgInfo.getFromQQ(),
                            keyWordFunction.dealWithText(personMsgInfo.getFromQQ(), personMsgInfo.getMsg()));
                    break;
                }
            }
        }
    }
}
