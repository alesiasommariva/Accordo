package com.project.accordo.Entity;

public class Channel {
    private String ctitle;
    private boolean mine;

    public Channel(String ctitle, boolean mine) {
        this.ctitle = ctitle;
        this.mine = mine;
    }

    public String getCtitle() {
        return ctitle;
    }

    public void setCtitle(String ctitle) {
        this.ctitle = ctitle;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "ctitle='" + ctitle + '\'' +
                ", mine=" + mine +
                '}';
    }
}
