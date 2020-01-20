game
====

This project implements the game of Breakout.

Name: Tharun Raj Mani Raj

### Timeline

Start Date: 01/13/2020

Finish Date: 01/20/2020

Hours Spent: 18 hours

### Resources Used
- stackoverflow.com
- tutorialspoint.com
- docs.oracle.com
- geeksforgeeks.org
- google.com


### Running the Program

Main class: GameDriver.java

Data files needed: "ball.gif", "brick7.gif", "brick4.gif", "brick8.gif", "aliens.gif"

Key/Mouse inputs:
- left and right arrow keys to move the paddle.
- G to start the game.
- Mouse inputs to interact with the buttons.

Cheat keys:
- 1,2,3 : jump to level 1, 2 and 3 respectively
- L: adds one life
- R: Resets the position and the direction of the ball, and the paddle.
- M: Double the paddle size
- N: Reduce the paddle size by a half
- F: Increase the speed of the ball
- S: Reduce the speed of the ball

Known Bugs:
- Occasionally when the ball hits a brick, it counts it as more than 1 hit.
Therefore occasionally the bricks that require more than 1 hit are destroyed in 1 hit. The scores are still appropriately awarded despite this issue (1 point per hit).
- The ball is not deflected when it touches the sides of the paddle, and it appears to pass through the paddle on the very far ends.
This rarely happens during gameplay though.

Extra credit:
- As mentioned in the plan, the added feature is the crossover between space invaders and breakout.
When a brick is hit, there is a 1/5 probability that an alien will fall from the top of the screen.
The player needs to catch the alien before it reaches the bottom of the screen or they will lose a life.
If they catch the alien their score is increased by 20.
- I found this implementation to be better than allowing the ball to destroy the aliens instead because the players play a more active role in
destroying the aliens through this implementation.
- Also, instead of making this feature optional, I applied it to the whole game since it seemed to be really fun.

### Notes/Assumptions
- I used java 13 for this project, and some of the functionalities may not be available in previous versions of java.
- One interesting issue I had was using a timer for the power ups. Sometimes it led to some buggy gameplay where a portion of the screen would be blacked out.
To overcome this, I changed my timer implementation from TimerTask to the Platform.runLater implementation and the bug seemed to be fixed.
- The ball speed and paddle speed increases respective to the level currently being played.

### Impressions
- This project was definitely an experience. I spent a decent amount of time working on this project, but every step of the way was more interesting than the last. I learned so much
in developing a working application and I am definitely satisfied with this outcome given the fact that I had no previous development experience.
- I feel that as I accumulate more development experience, I would be able to make this game more robust with many more advanced features.

