package com.alipay.sofa.entrance.web.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sofa.constant.IsoFields;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;

@Service
public class MessageService {

    private Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Resource
    private MessageFactory<IsoMessage> defaultMessageFactory;

    /**
     * Create an {@link IsoMessage} from json.
     *
     * @param json
     * @return
     */
    public IsoMessage of(JSONObject json) {
        Integer type = parseType(json.getString("0"));
        logger.info(String.format("Type %04x", type));
        IsoMessage isoMessage = defaultMessageFactory.newMessage(type);
        for (int i = 2; i < 129; i++) {
            String key = String.valueOf(i);
            String value = json.getString(key);
            if (StringUtils.isNotBlank(value)) {
                IsoFields field = IsoFields.of(i);
                logger.info(String.format("Field %-4s %45s %-14s", field, field.getDesc(), value));
                isoMessage.setValue(i, value, field.getFormat(), field.getLength());
            }
        }
        return isoMessage;
    }

    private int parseType(String type) {
        if (type.length() % 2 == 1) {
            type = "0" + type;
        }
        if (type.length() != 4) {
            return -1;
        }
        return ((type.charAt(0) - 48) << 12) | ((type.charAt(1) - 48) << 8)
                | ((type.charAt(2) - 48) << 4) | (type.charAt(3) - 48);
    }


    public JSONObject loadTemplatesFromResource() {
        URL url = this.getClass().getClassLoader().getResource("j8583-templates.xml");
        try (InputStream stream = url.openStream()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            logger.info("load message templates from : {}", url);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(stream));
            final Element root = doc.getDocumentElement();
            return parseTemplates(root.getElementsByTagName("template"));
        } catch (Exception e) {
            logger.error("error occur when load xml templates",e);
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
