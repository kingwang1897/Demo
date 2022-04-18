package com.alipay.sofa.entrance.web.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URL;

@Service
public class MessageService {

    private Logger logger = LoggerFactory.getLogger(getClass());


    public JSONObject loadTemplatesFromResource() throws IOException {
        URL resource = this.getClass().getClassLoader().getResource("j8583-templates.xml");
        return parse(resource.getPath());
    }


    public JSONObject parse(String uri) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(uri);
            final Element root = doc.getDocumentElement();
            return parseTemplates(root.getElementsByTagName("template"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject parseTemplates(
            final NodeList nodes) {
        JSONObject templates = new JSONObject(true);
        for (int i = 0; i < nodes.getLength(); i++) {
            Element elem = (Element) nodes.item(i);
            String mti = elem.getAttribute("type");
            JSONObject template = new JSONObject(true);
            JSONObject mtiJson = new JSONObject(true);
            mtiJson.put("value", mti);
            template.put("0", mtiJson);
            templates.put(mti, template);
            parseField(elem, template);
        }
        return templates;
    }

    private void parseField(Element elem, JSONObject template) {
        NodeList fields = elem.getElementsByTagName("field");
        for (int j = 0; j < fields.getLength(); j++) {
            Element f = (Element) fields.item(j);
            if (f.getParentNode() == elem) {
                final int num = Integer.parseInt(f.getAttribute("num"));
                final String type = f.getAttribute("type");
                JSONObject field = new JSONObject(true);
                field.put("num", num);
                field.put("type", type);
                String lens = f.getAttribute("length");
                if (StringUtils.isNotEmpty(lens)) {
                    final int length = Integer.parseInt(lens);
                    field.put("length", length);
                }
                NodeList subs = f.getElementsByTagName("field");
                if (subs.getLength() > 0) {
                    //TODO
                    //Children not implement
                    logger.warn("Field " + num + " parse not implement.");
                } else {
                    field.put("value", f.getTextContent());
                    template.put(String.valueOf(num), field);
                }
            }
        }
    }

}
