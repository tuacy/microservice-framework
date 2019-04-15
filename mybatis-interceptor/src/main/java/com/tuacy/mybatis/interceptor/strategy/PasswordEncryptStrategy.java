package com.tuacy.mybatis.interceptor.strategy;

import com.tuacy.mybatis.interceptor.interceptor.encryptresultfield.IEncryptResultFieldStrategy;
import org.springframework.util.DigestUtils;

public class PasswordEncryptStrategy implements IEncryptResultFieldStrategy {
    @Override
    public String encrypt(String source) {
        if (source == null) {
            return null;
        }
        return new String(DigestUtils.md5Digest(source.getBytes()));
    }


}
