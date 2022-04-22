package com.alipay.sofa.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

public final class Asserts {

    private static Logger LOGGER = LoggerFactory.getLogger(Asserts.class);

    public static void notEmpty(Collection collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            LOGGER.error(message);
            throw new RuntimeException(message);
        }
    }

    private Asserts() {
    }

}
