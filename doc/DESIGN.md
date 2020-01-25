DESIGN.md
- Individual project by Tharun Raj Mani Raj (tm272)
- I wanted to make it easy to add new powerups to the game. Since I used essentially only one class for this, I made almost all the important attributes of the game instance variables so that all the different powerup functions could access them easily.
- There are 2 classes in the project: 
    - A Brick class which makes a brick object, and during a brick's instantiation it randomly assigns a powerup as well.
    - The GameDriver class which essentially handles the control flow of the entire game.
- As of now, adding new features would just mean adding new functions to the GameDriver class to handle the logic of the new functionalities. I would like this to change so that we make a new class to implement the changes instead of just adding more functions to the code.
- The biggest design flaw that I think the project has is that all the functions are clumped up into one class and I did not have the time to refactor since duration given to complete the project was really limited.
