# Snake_Movement
Terminal snake movement program in java

Snake class : 
  Snake object represents a snake in a grid(matrix) which can move in four directions(top,right,bottom,left).
  Initially it has lenght of 1. 
  The snake dies if it collide with wall(out of matrix) or itself.
  The length of the Snake increases if it consumes one of the generated food(by collision with food). 
  It has various functions like:
    generateFood : to generate given number of foods in matrix.
    move : to move one step in given direction.
    getMovableDirections : to get the directions that the current snake can move without dying.
    getState : get a character matrix representing the current state of snake and foods. 
    isAlive : returns true if the snake is not yet died.

SnakeMovement class:
    Its just a class to create a single snake object and display the state in console(erasing entire console every time before printing state).
    It moves the snake in the direction (movable direction) which leads to the closest food.
    If no food is available then it moves the snake in random direction.
