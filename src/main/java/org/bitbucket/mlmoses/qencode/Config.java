package org.bitbucket.mlmoses.qencode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class Config {
    private static final int DEFAULT_SIZE = 512;
    private static final int DEFAULT_VERBOSITY = 1;

    static Config parse(String[] args) {
        final var cb = new Config.Builder();
        cb.setVerbosity(DEFAULT_VERBOSITY);

        var state = State.NONE;
        var noMoreArgs = false;
        for (var a : args) {
            if ("-h".equals(a) || "--height".equals(a)) {
                state = State.HEIGHT;
            } else if ("-q".equals(a) || "--quiet".equals(a)) {
                cb.setVerbosity(0);
                state = State.NONE;
            } else if ("-v".equals(a) || "--verbosity".equals(a)) {
                state = State.VERBOSITY;
            } else if ("-V".equals(a) || "--version".equals(a)) {
                cb.setShowVersion(true);
                state = State.NONE;
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

        var height = cb.getHeight();

        if (cb.getWidth() == -1)
            cb.setWidth(height == -1 ? DEFAULT_SIZE : height);

        if (height == -1)
            cb.setHeight(cb.getWidth());

        return cb.build();
    }

    private final int height, verbosity, width;
    private final boolean showVersion;
    private final List<String> paths;

    private Config(Builder builder) {
        this.height = builder.height;
        this.showVersion = builder.showVersion;
        this.verbosity = builder.verbosity;
        this.width = builder.width;

        final var pathCount = builder.paths.size();
        if (pathCount == 0)
            this.paths = Collections.emptyList();
        else if (pathCount == 1)
            this.paths = Collections.singletonList(builder.paths.get(0));
        else
            this.paths = Collections.unmodifiableList(builder.paths);
    }

    int getHeight() {
        return height;
    }

    boolean getShowVersion() {
        return showVersion;
    }

    List<String> getPaths() {
        return paths;
    }

    int getVerbosity() {
        return verbosity;
    }

    int getWidth() {
        return width;
    }

    private final static class Builder {
        private int height, verbosity, width;
        private boolean showVersion;
        private final List<String> paths;

        Builder() {
            this.height = -1;
            this.paths = new ArrayList<>();
            this.verbosity = -1;
            this.width = -1;
        }

        void addPaths(String first, String... more) {
            if (first != null && !first.isEmpty())
                paths.add(first);
            if (more != null) {
                for (String path : more) {
                    if (path != null && !path.isEmpty())
                        paths.add(path);
                }
            }
        }

        Config build() {
            return new Config(this);
        }

        int getHeight() {
            return height;
        }

        int getWidth() {
            return width;
        }

        void setHeight(int height) {
            this.height = height < 0 ? 0 : height;
        }

        void setShowVersion(boolean showVersion) {
            this.showVersion = showVersion;
        }

        void setVerbosity(int verbosity) {
            this.verbosity = verbosity < 0 ? 0 : verbosity;
        }

        void setWidth(int width) {
            this.width = width < 0 ? 0 : width;
        }
    }

    private enum State {
        NONE,
        HEIGHT,
        VERBOSITY,
        WIDTH
    }
}
