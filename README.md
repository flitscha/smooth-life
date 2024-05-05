# smooth-life
Generalization of Convay's Game of Life

based on this paper: https://arxiv.org/abs/1111.1567

my implementation provides a grid-editor to paint the start state of the grid, as well as a function-editor to draw the transfer function.


# How does it work?
To put it simply, you have a grid with values between 0 and 1. The transfer function determines how the grid changes in the next iteration.
The function is applied to every single cell in the grid, and at the end the grid is overwritten with the new values.

In the Start menu of the program there are two settings: 'inner-radius' and 'outer-radius'. These are to be understood as radii around the individual cells.
For each individual cell, the sum of all values that are in the inner (or outer) radius are added together.

These two values are the inputs for the transfer function. For each pair of inner-sum and outer-sum, a value between 0 and 1 is assigned by the function.
This means that the neighboring elements of a cell influence the value of the cell in the next iteration.

To make the process even smoother, I interpret the function values, not directly as the next value, but as the difference from the current value. To do this, I scale the function values between -1 and 1. (more details are in the paper)


# example
https://github.com/flitscha/smooth-life/assets/166940877/ce69a74f-0a8b-48ab-85a2-0782c6af0796


This is my first java project. That's why it might be a bit chaotic. And the simulation is quite slow because it's running on the CPU. In the simulation, a lot of things are unnecessarily double-calculated. I'm sure it would be possible to run a lot faster on the CPU as well.
