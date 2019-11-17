package me.shrikanthravi.songstudio.data.model;

public class MessageEvent {
    boolean load=false;
    int pos;

    public MessageEvent(boolean load, int pos) {
        this.load = load;
        this.pos = pos;
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
