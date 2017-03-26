package com.dd7.lurkerhn.rest.model;

public class Comment extends Item {

    private int padding = 0;


    public Comment(Item item) {
        //TODO: ugly hack to get around indenting comments
        super(item.getId(), item.isDeleted(), item.getType(), item.getBy(), item.getTime(), item.getTimeFormatted(), item.getText(), item.isDead(), item.getParent(), item.getKids(), item.getUrl(), item.getScore(), item.getTitle());
    }

//    Padding is kept here so that comments are displayed as a conversation, with padding according to their nest level.
//    It is an ugly workaround but it's the easiest solution
    public int getPadding() {
        return padding;
    }

    public void increasePadding(int padding) {
        this.padding = padding + 1;
    }
}