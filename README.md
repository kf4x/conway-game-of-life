# Game of Life


## Overview

This is the parallel version of Game of Life in java



## Enhancements

* Using a volatile image, this allows me to draw on a hardware accelerated 
image. this takes some of the load from the processor.

* Using standard .rle file [format](http://www.conwaylife.com/wiki/Run_Length_Encoded)

* Hold down shift to zoom in further.

* Not drawing dead cells. The dead cells are represented as a black 
background. The grid lines are highlighted with spaces from the drawing of 
alive cells.

* Choose your threading style! you can choose from ForkJoin, ExecuterService,
 or SimpleThreading.
 

## Known bugs
* Currently the zoom and "viewport's" state are saved which causes a index 
error if a new bord is created that is smaller than where the view's current 
state is. i.e. if I have dragged my bord to the left of looking at the 1000th
column and i create a bord that is 100x100 an error will occur
 
* mouse events are kept queued as FIFO its very annoying to zoom and if there
 are any zooms left on the stack and the shift key is pressed, it will 
 zoom out.

* After selection many presets the grid no longer draws properly.
 
* The next button is not ran on a thread. This was initially on a separate 
thread however if the user continues to click it creates a many calls for the
 connection to run thus, inadvertently holding up the UI.
