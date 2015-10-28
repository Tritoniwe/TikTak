package com.glazunov.tiktak.controller;


public class Move {
    private final String position;
    private final boolean owin;
    private final boolean xwin;
    private final boolean drawn;

    public Move(String position, boolean owin, boolean xwin, boolean drawn) {
        this.position = position;
        this.owin = owin;
        this.xwin = xwin;
        this.drawn = drawn;
    }

    public String getPosition() {
        return position;
    }

    public boolean isDrawn() {
        return drawn;
    }

    public boolean isOwin() {
        return owin;
    }

    public boolean isXwin() {
        return xwin;
    }

}
