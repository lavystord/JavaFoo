package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.NotNull;

/**
 * Created by Dell on 2015/8/27.
 */
public class WordInfo {
    @NotNull
    Long keyId;


    String constantLockWord;

    String disposableLockWord;

    @JsonView({View.MyKeyDetail.class})
    String constantKeyWord;

    @JsonView({View.MyKeyDetail.class})
    String disposableKeyWord;

    public String getConstantLockWord() {
        return constantLockWord;
    }

    public void setConstantLockWord(String constantLockWord) {
        this.constantLockWord = constantLockWord;
    }

    public String getDisposableLockWord() {
        return disposableLockWord;
    }

    public void setDisposableLockWord(String disposableLockWord) {
        this.disposableLockWord = disposableLockWord;
    }

    public String getConstantKeyWord() {
        return constantKeyWord;
    }

    public void setConstantKeyWord(String constantKeyWord) {
        this.constantKeyWord = constantKeyWord;
    }

    public String getDisposableKeyWord() {
        return disposableKeyWord;
    }

    public void setDisposableKeyWord(String disposableKeyWord) {
        this.disposableKeyWord = disposableKeyWord;
    }

    public Long getKeyId() {

        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }
}
