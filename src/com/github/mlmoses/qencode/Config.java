package com.github.mlmoses.qencode;

import java.util.Collection;
import java.util.LinkedList;

final class Config {

    private int height, width;
    private Collection<String> paths;

    public int getHeight() {
        return height;
    }

    public Collection<String> getPaths() {
        if (paths == null) {
            paths = new LinkedList<>();
        }
        return paths;
    }

    public int getWidth() {
        return width;
    }

    public Config setHeight(int height) {
        this.height = height < 0 ? 0 : height;
        return this;
    }

    public Config setPaths(Collection<String> paths) {
        if (paths != null) {
            if (this.paths != null) {
                this.paths.addAll(paths);
            } else {
                this.paths = paths;
            }
        }
        return this;
    }

    public Config setWidth(int width) {
        this.width = width < 0 ? 0 : width;
        return this;
    }

}
