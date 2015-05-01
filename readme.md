#jvARM-fxgui

This project is a GUI front end for [jvARM core](https://github.com/guygrigsby/jvARM)

##Build
jvARM-fxgui was developed using:
* [Eclipse](https://eclipse.org/) 
* [e(fx)clipse plugin for Eclipse](https://www.eclipse.org/efxclipse/index.html)
* Java FX Scene Builder (Included in Java JDK 8+)

The easiest way to build is to open both jvARM and jvARM-fxgui in Eclipse and export jvARM-fxgui to a runnable jar. jvARM is a dependency for jvARM-fxgui and Eclipse will compile everything into a single runnable jar file.

##Download
A binary build may be downloaded from the github repo at https://github.com/guygrigsby/jvARM-fxgui/releases

##How to use
One of the goals of jvARM was to make an easy to use environment for learning ARM. Thus, There are very few steps to get started. Currently the platform only supports individual instructions, not data and code sections. For example
```assembly
MOV r0, #0
MOV r1, #1
MOV r12, #10
loop
ADD r2, r1, r0
MOV r0, r1
MOV r1, r2
SUB r12, r12, #1
CMP r12, #0
BNE loop
```
rather than
```assembly
        AREA     ARMex, CODE, READONLY
                                ; Name this block of code ARMex
        ENTRY                   ; Mark first instruction to execute
MOV r0, #0
MOV r1, #1
MOV r12, #10
loop
ADD r2, r1, r0
MOV r0, r1
MOV r1, r2
SUB r12, r12, #1
CMP r12, #0
BNE loop
        END                     ; Mark end of file
```
1. Open the runnable jar. (Either [download](https://github.com/guygrigsby/jvARM-fxgui/releases) a prebuilt copy or build yourself)
2. The editor opens with some sample code that computes 10 numbers in the Fibonacci sequence. You can use this, write your own assembly, or open a file with File > Open.
3. Press the "Start Debug" button.
4. Use the "Step" button to execute a single line at a time. 

Important notes:
* Memory is not implemented yet.
* Where applicable implemented instructions support flexible second operands
* New instructions are being implement all the time. Implemented instructions are:
  * `ADD`
  * `SUB`
  * `RSB`
  * `ADC`
  * `SBC`
  * `RSC`
  * `AND`
  * `ORR`
  * `EOR`
  * `BIC`
  * `B` (with all conditional codes)
  * `CMP`
  * `MOV`

##License
This project is licensed under the [GNU General Public License](https://www.gnu.org/copyleft/gpl.html)
