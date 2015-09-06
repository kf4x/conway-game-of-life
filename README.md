# Game of Life


## Overview

This is the parallel version of Game of Life in java
This has the 



#Known bugs
* Currently the zoom and "viewport's" state are saved which causes a index 
error if a new bord is created that is smaller than where the view's current 
state is. i.e. if I have dragged my bord to the left of looking at the 1000th
column and i create a bord that is 100x100 an error will occur
 
* mouse events are kept queued as FIFO its very annoying to zoom and if there
 are any zooms left on the stack and the shift key is pressed, it will 
 zoom out.
   
