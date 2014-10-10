package org.bitbucket.mlmoses.qencode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

final class Config {

    private static final int DEFAULT_SIZE = 512;
    private static final int DEFAULT_VERBOSITY = 1;

    static final Config parse(String[] args) {
        final Config.Builder cb = new Config.Builder()
            .setVerbosity(DEFAULT_VERBOSITY);

        State state = State.NONE;
        boolean noMoreArgs = false;
        for (String a : args) {
            if ("-h".equals(a) || "--height".equals(a)) {
                state = State.HEIGHT;
            } else if ("-q".equals(a) || "--quiet".equals(a)) {
                cb.setVerbosity(0);
                state = State.NONE;
            } else if ("-v".equals(a) || "--verbosity".equals(a)) {
                state = State.VERBOSITY;
            } else if ("-w".equals(a) || "--width".equals(a)) {
                state = State.WIDTH;
            } else if (a.startsWith("-")) {
                if (noMoreArgs) {
                    cb.addPaths(a);
                } else if ("--".equals(a)) {
                    noMoreArgs = true;
                }
            } else {
                switch (state) {
                    case HEIGHT:
                        cb.setHeight(Integer.parseInt(a));
                        break;
                    case VERBOSITY:
                        cb.setVerbosity(Integer.parseInt(a));
                        break;
                    case WIDTH:
                        cb.setWidth(Integer.parseInt(a));
                        break;
                    default:
                        cb.addPaths(a);
                        break;
                }
                state = State.NONE;
            }
        }

        int height = cb.getHeight();

        if (cb.getWidth() == -1)
            cb.setWidth(height == -1 ? DEFAULT_SIZE : height);

        if (height == -1)
            cb.setHeight(cb.getWidth());

        return cb.build();
    }

    private final int height, verbosity, width;
    private final List<String> paths;

    private Config(Builder builder) {
        this.height = builder.height;
        this.verbosity = builder.verbosity;
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

    public int getVerbosity() {
        return verbosity;
    }

    public int getWidth() {
        return width;
    }

    private final static class Builder {

        private int height, verbosity, width;
        private final List<String> paths;

        Builder() {
            this.height = -1;
            this.paths = new ArrayList<>();
            this.verbosity = -1;
            this.width = -1;
        }

        Builder addPaths(String first, String... more) {
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

        Builder addPaths(Collection<String> paths) {
            if (paths != null) {
                for (String path : paths) {
                    if (path != null && !path.isEmpty())
                        paths.add(path);
                }
            }
            return this;
        }

        Config build() {
            return new Config(this);
        }

        int getHeight() {
            return height;
        }

        int getVerbosity() {
            return verbosity;
        }

        int getWidth() {
            return width;
        }

        Builder setHeight(int height) {
            this.height = height < 0 ? 0 : height;
            return this;
        }

        Builder setVerbosity(int verbosity) {
            this.verbosity = verbosity < 0 ? 0 : verbosity;
            return this;
        }

        Builder setWidth(int width) {
            this.width = width < 0 ? 0 : width;
            return this;
        }

    }

    private static enum State {
        NONE,
        HEIGHT,
        VERBOSITY,
        WIDTH
    }

}
