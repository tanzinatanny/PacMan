Pacman Game :

The App class is only handling App initialization and Scene creation.

The PaneOrganizer class is organizing all the panes as needed.

Then we have SmartSquare class that contains all the info about Squares in the board. We use this class to generate the board and place
all objects in their places with the help of SupportMap data. Just in case of PACMAN_LOCATION and GHOST_LOCATION we use the created static Pacman
and Ghost instance of pinky and place them in proper location provided.

In our Game class we have a single timeline that handles all the movements of Ghosts and Pacman. 
For managing the ghost modes we have used a cycle counter which is incremented at each cycle and then we use it to switch between Scatter and Chase mode of Ghosts, we also control Frightened mode
time by using another frightenedModeCounter. Without using these counters we would have needed two separate timelines but now we only have one.

We created two queue for active ghosts and inactive ghosts in our Game class, in the timeline handle we release each Ghost after a fixed cycle count according to the order
they were eaten by the pacman.

In Chase mode we track pacman and it's surrounding squares by each ghosts as mentioned in the hand out.
In Scatter mode our ghosts goes to different corners. 
In Frightened mode we have generated only 1 random position and placed targets for each of the ghosts relatively.



 