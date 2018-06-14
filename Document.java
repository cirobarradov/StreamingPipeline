package com.ferrovial.digitalhub.twitter;

public class Document {
    public String id, language, text;

    public Document(String id, String language, String text){
        this.id = id;
        this.language = language;
        this.text = text;
    }
    public Document(String id, String text){
        this.id = id;
        this.text = text;
    }
}
