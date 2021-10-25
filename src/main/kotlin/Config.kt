package com.salamanderlive.qencode

private const val DEFAULT_SIZE = 512
private const val DEFAULT_VERBOSITY = 1

// FIXME Implement an ImmutableList for Config.unknownArgs and Config.paths

class Config(args: Array<String>) {
    val height: Int
    val paths: List<String>
    val showVersion: Boolean
    val unknownArgs: List<String>
    val verbosity: Int
    val width: Int

    init {
        var height = -1
        var width = -1
        var verbosity = DEFAULT_VERBOSITY
        var showVersion = false
        var state = State.NONE
        var noMoreArgs = false
        val paths = mutableListOf<String>()
        val unknownArgs = mutableListOf<String>()

        for (a in args) {
            when (a) {
                "--height", "-h" -> state = State.HEIGHT
                "--quiet", "-q" -> {
                    verbosity = 0
                    state = State.NONE
                }
                "--verbosity", "-v" -> state = State.VERBOSITY
                "--version", "-V" -> {
                    showVersion = true
                    state = State.NONE
                }
                "--width", "-w" -> state = State.WIDTH
                "--" -> {
                    noMoreArgs = true
                    state = State.NONE
                }
                else -> {
                    when (state) {
                        State.HEIGHT -> height = a.toInt()
                        State.VERBOSITY -> verbosity = a.toInt()
                        State.WIDTH -> width = a.toInt()
                        else -> {
                            if (noMoreArgs || !a.startsWith("-"))
                                paths.add(a)
                            else
                                unknownArgs.add(a)
                        }
                    }
                    state = State.NONE
                }
            }
        }

        if (width == -1)
            width = if (height == -1) DEFAULT_SIZE else height

        if (height == -1)
            height = width

        this.height = height
        this.paths = paths
        this.showVersion = showVersion
        this.unknownArgs = unknownArgs
        this.verbosity = verbosity
        this.width = width
    }
}

private enum class State {
    NONE,
    HEIGHT,
    VERBOSITY,
    WIDTH
}
