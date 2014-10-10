package com.github.mlmoses.qencode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

final class Config {

    private final int height, width;
    private final List<String> paths;

    private Config(Builder builder) {
        this.height = builder.height;
        this.width = builder.width;

        final int pathCount = builder.paths.size();
        if (pathCount == 0)
            this.paths = Collections.emptyList();
        else if (pathCount == 1)
            this.paths = Collections.singletonList(builder.paths.get(0));
        else
            this.paths = Collections.unmodifiableList(builder.paths);
    }

    public int getHeight() {
        return height;
    }

    public List<String> getPaths() {
        return paths;
    }

    public int getWidth() {
        return width;
    }

    final static class Builder {

        private int height, width;
        private final List<String> paths = new ArrayList<>();

        public Builder addPaths(String first, String... more) {
            if (first != null && !first.isEmpty())
                paths.add(first);
            if (more != null) {
                for (String path : more) {
                    if (path != null && !path.isEmpty())
                        paths.add(path);
                }
            }
            return this;
        }

        public Builder addPaths(Collection<String> paths) {
            if (paths != null) {
                for (String path : paths) {
                    if (path != null && !path.isEmpty())
                        paths.add(path);
                }
            }
            return this;
        }

        public Config build() {
            return new Config(this);
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public Builder setHeight(int height) {
            this.height = height < 0 ? 0 : height;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width < 0 ? 0 : width;
            return this;
        }

    }

}
