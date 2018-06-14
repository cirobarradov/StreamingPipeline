package com.ferrovial.digitalhub.twitter;

import java.util.ArrayList;
import java.util.List;

public class Documents {
    public List<Document> documents;

    public Documents() {
        this.documents = new ArrayList<Document>();
    }
    public void add(String id, String language, String text) {
        this.documents.add (new Document (id, language, text));
    }
    public void add(String id, String text) { this.documents.add (new Document (id, text)); }
}