package top.yzlin.beichen.keywordfunc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import top.yzlin.cqrobotsdk.CQRobot;
import top.yzlin.cqrobotsdk.cqinfo.GroupMsgInfo;
import top.yzlin.cqrobotsdk.cqinfo.PersonMsgInfo;
import top.yzlin.cqrobotsdk.msginterface.reply.GroupMsgReply;
import top.yzlin.cqrobotsdk.msginterface.reply.PersonMsgReply;
import top.yzlin.tools.Tools;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Component
public class KeyWord implements GroupMsgReply{
    private String groupId;
    private KeyWordDoc keyWordDoc;

    @Value("${cqrobot.groupId}")
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Autowired
    public void setKeyWordDoc(KeyWordDoc keyWordDoc) {
        this.keyWordDoc = keyWordDoc;
    }

    public void addKeyWord(String key, String value){
        keyWordDoc.saveKeyWord(key,value);
    }

    public void removeKeyWord(String key){
        keyWordDoc.removeKeyWord(key);
    }

    public boolean containsKeyWord(String key){
        return keyWordDoc.containsKeyWord(key);
    }

    public String getKeyWord(String key){
        return keyWordDoc.getKeyWord(key);
    }

    public void addAdmin(String admin){
        keyWordDoc.addAdmin(admin);
    }

    public void removeAdmin(String admin){
        keyWordDoc.removeAdmin(admin);
    }

    public int size(){
        return keyWordDoc.size();
    }

    public Set<String> keySet(){
        return keyWordDoc.keySet();
    }

    public boolean containsAdmin(String admin){
        return keyWordDoc.containsAdmin(admin);
    }


    @Override
    public boolean fromGroup(String from) {
        return groupId.equals(from);
    }

    @Override
    public boolean checkMsg(String from) {
        return containsKeyWord(from);
    }

    @Override
    public String replyMsg(GroupMsgInfo groupMsgInfo) {
        return getKeyWord(groupMsgInfo.getMsg());
    }
}
