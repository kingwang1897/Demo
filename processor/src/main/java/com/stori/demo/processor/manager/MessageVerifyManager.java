package com.stori.demo.processor.manager;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageVerifyManager {
    protected final Logger logger = LoggerFactory.getLogger(MessageVerifyManager.class);

    private final String URL_PKT = "https://iso8583.info/lib/CUP/Online/pkt";

    private final String URL_MSG = "https://iso8583.info/lib/CUP/Online/msg";

    public void parse(String testMessage) {
        parse(testMessage, URL_PKT);
    }

    public void parse(String testMessage, String type) {
        logger.info("iso8583.info parsing {}", testMessage);
        String urlType = type == null ? URL_PKT : URL_MSG;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("s", testMessage)
                .build();
        Request request = new Request.Builder()
                .url(urlType)
                .addHeader("POST", "/lib/CUP/Online/msg HTTP/1.1")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Length", "134")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cookie", "uid=PnHQYGJVWfkKsxO4AwMFAg==; G_ENABLED_IDPS=google")
                .addHeader("Host", "iso8583.info")
                .addHeader("Origin", "https://iso8583.info")
                .addHeader("Referer", "https://iso8583.info/lib/CUP/Online/msg")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.88 Safari/537.36")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "Windows")
                .addHeader("uid", "PnHQYGJVWfkKsxO4AwMFAg==")
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            logger.info("iso8583.info post to {}", urlType);
            Response response = call.execute();
            logger.info("iso8583.info Res:{}", response);
            String m = response.body().string();
            if (m.contains("textarea")) {
                logger.info("iso8583.info Parsed: {}", m.split("<.*textarea.*>?")[1]);
                System.out.println(m.split("<.*textarea.*>?")[1]);
            } else {
                logger.error("iso8583.info Parsed not found please change cookie.");
            }
        } catch (IOException e) {
            logger.error("iso8583.info test error:", e);
        }
    }
}
