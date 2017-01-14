package com.dd7.yahn.rest.model;

public class Comment extends Item {

    private int padding = 0;


    public Comment(Item item) {
        //TODO: ugly workaround, should check to see if it's possible to make it elegant
        super(item.getId(), item.isDeleted(), item.getType(), item.getBy(), item.getTime(), item.getTimeFormatted(), item.getText(), item.isDead(), item.getParent(), item.getKids(), item.getUrl(), item.getScore(), item.getTitle());
    }

    public int getPadding() {
        return padding;
    }

    public void increasePadding(int padding) {
        this.padding = padding + 1;
    }
}
