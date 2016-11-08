package chouti.mode;

/*******************************************************************************
 * Copyright (c) 2005-2016 Gozap, Inc.
 * Contributors:
 * xiaoming  on 16-11-1.
 *******************************************************************************/
public class TextSimHashBean {



    private String text;

    private String hash;


    public TextSimHashBean(String text,String hash){
        this.text = text;
        this.hash = hash;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
