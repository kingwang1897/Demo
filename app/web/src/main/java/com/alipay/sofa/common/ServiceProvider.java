package com.alipay.sofa.common;

public interface ServiceProvider<T, S extends Typeable<T>> {
    S getService(T type);
}
