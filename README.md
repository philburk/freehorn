# FreeHorn, by Larry Polansky

FreeHorn is a computer music application, meant to accompany live instrumentalists. It has been performed many times over the past 10 years in a wide variety of circumstances and musical configurations.

The software allows users to set parameters (time, harmonic progression, rhythmic and density variables) that control the form of the piece. However, the piece has its own particular form: the continuous modulation of harmonic series' based on different fundamentals. This idea is taken from Polansky's Psaltery set of works (see his website for more examples).

Code for freeHorn was originally written by Phil Burk (with Larry Polansky) in Java, using Burk's Jsyn as the synthesis engine. Mike Winter contributed some modifications and features to the software some years later.

Some archival, live recordings are available at //http://eamusic.dartmouth.edu/~larry/archive.recordings.html

Various other notes (a partial manual) are at http://eamusic.dartmouth.edu/~larry/freeHorn/index.html

## To Run FreeHorn

A precompiled version of FreeHorn is included in the Git repo.

You need to install a Java JDK or JRE before running FreeHorn.
Then enter:

    cd freehorn
    java -jar FreeHorn.java

## To Build FreeHorn

Install ANT and a Java JDK.

Enter:

    cd freehorn
    ant
    
A complete JAR file that contains FreeHorn and JSyn will be built and placed in the "dist/lib" folder.
The name of the JAR file will have a datestamp. So it keeps changing.
To run it, enter:

    ls dist/lib  # check the JAR name
    java -jar dist/lib/FreeHorn-{datestamp}.jar
