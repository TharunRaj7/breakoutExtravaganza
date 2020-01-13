# Game Plan
## THARUN RAJ MANI RAJ


### Variant: Super Breakout (With Space Invaders Crossover)

### General Level Descriptions:
#### For all levels, 3 lives will be given for each level and scores will be recorded per brick hit.
    Level 1:
        - Standard rectangular-like arrangement with a row of hard bricks on the top and center rows (see in next section for bricks discription). 
    
    Level 2:
        - An upside down triangle with the edges (fences) consisting of hard bricks.
        - To increase the difficulty, the bricks are placed lower on the screen.
        - The ball speed is also increased
    Level 3:
        - A rectangular-like arrangement with hard bricks on every other row (might change hard brick placement later).
        - Ball speed is increased to increase difficulty.
        - The bricks are placed lower on the screen than the previous level (harder than the previous one because there will more bricks on the bottom-most row instead of just the tip of a triangle like the arrangement in the previous level).
### Bricks Ideas
- Standard Brick: Breaks in one hit
- Hard Brick: Breaks in 3 hits
- Power-up Brick: Drops a power-up when it breaks (Consists of both soft and hard breaks)

**The power-ups are assigned randomly to the bricks at the level setup*

### Power Up Ideas
- Extra Life: adds a life for the player (max is 5 lifes at any given time, more life power-ups are ignored)
- Paddle Roids: extends the length of the paddle for limited time
- Ball Spawner: creates more balls from the position of the current ball. As long there is one ball on the screen the player will not lose a life.
- Ball acid: slows down the speed of the ball for a limited time.

### Cheat Key Ideas
- Win and skip level with an arbitrary score.
- lose the game
- Show all the bricks with power-ups (shade them)
- slow down ball speed
- Begin crossover protocol (Only in crossover mode)


### Something Extra
The added feature is an additional game mode called "Crossover mode." In this mode, on each level, after an interval of brick hits, "aliens" will start to appear from the top of the screen which the player needs to kill before they reach the bottom, in which case the player loses a life for each alien that does. The player kills the aliens using the same ball. An optional feature would be to add lasers that continuously shoot to kill the aliens while the player simultaneously tries to break all the bricks.

This is a harder gameplay option but definitely more fun. The name of this mode is inspired by the fact that this gameplay option is essentially a ***crossover*** between Breakout and Space Invaders.
