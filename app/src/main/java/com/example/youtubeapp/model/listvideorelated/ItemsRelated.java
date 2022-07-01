package com.example.youtubeapp.model.listvideorelated;

public class ItemsRelated {
    public String kind;
    public String etag;
    public Id id;
    public SnippetRe snippet;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public SnippetRe getSnippet() {
        return snippet;
    }

    public void setSnippet(SnippetRe snippet) {
        this.snippet = snippet;
    }
}
