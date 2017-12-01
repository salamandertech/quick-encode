#####################
QR Code Quick Encoder
#####################

This is a small utility which will encode anything you want (within the limits
of the specification) to a QR code. It doesn't do any processing on the input
though, so if you want something complex, like a Salamander tag, you'll have
to find another utility to generate the input. It's nice for creating triage
tags though.


Building
========

The project uses the Gradle wrapper, so once you have cloned the repository
there is no additional setup. Just run the build script::

    $ ./gradlew build

You can also use the install task to create an installable directory::

    $ ./gradlew install

This will create the ``build/install/quick-encode`` directory, which can be
copied to a suitable location in your file system. Just add the ``bin``
sub-directory to your path and you can run quick-encode from a command prompt.


Running
=======

If you just want to run the application, you have two options. First, you can
use the install task and execute the quick-encode script without copying the
directory (see the previous section). Second, you can use the run task::

    $ ./gradlew run my_barcode_contents.txt

Any positional arguments provided to the utility are treated as names of files
containing data to encode. Multiple files may be provided and they will all be
encoded to separate barcodes. Barcodes are output as PNGs where the file name
is the file name of the input with ".png" appended to the end.

It is possible to set the dimensions of the barcode with two command line
options. The width, in pixels, may be specified with ``--width`` or ``-w``,
while the height, also in pixels, may be specified with ``--height`` or
``-h``. The default is to output a PNG that is 512 pixels in both dimensions.
