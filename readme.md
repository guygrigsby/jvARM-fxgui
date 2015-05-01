#jvARM-fxgui

This project is a GUI front end for [jvARM core](https://github.com/guygrigsby/jvARM)

##Build
jvARM-fxgui was developed using:
* [Eclipse](https://eclipse.org/) 
* [e(fx)clipse plugin for Eclipse](https://www.eclipse.org/efxclipse/index.html)
* Java FX Scene Builder (Included in Java JDK 8+)

The easiest way to build is to open both jvARM and jvARM-fxgui in Eclipse and export jvARM-fxgui to a runnable jar. jvARM is a dependency for jvARM-fxgui and Eclipse will compile everything into a single runnable jar file.

##Download
link to download goes here

##How to use
One of the goals of jvARM was to make an easy to use environment for learning ARM. Thus, There are very few steps to get started. Currently the platform only supports individual instructions, not data and code sections. For example
1. Open the runnable jar. (Either [download]() a prebuilt copy or build yourself)
2. The editor opens with some sample code that computes 10 numbers in the Fibonacci sequence. You can use this, write your own assembly, or open a file with File > Open.
3. Press the "Start Debug" button.
4. Use the "Step" button to execute a single line at a time. 
