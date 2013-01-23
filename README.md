Augmenting a Monte Carlo Tree Search Agent for Ms Pac-Man with Machine Learning Techniques
===========================================================================================

This repository holds my fourth year individual project.  There isn't much here right now.

I'm teaching myself machine learning for this project using the [Coursera Machine Learning class](https://www.coursera.org/course/ml), so my attempts at the tutorial exercises are also uploaded here.

The project aims to write an extension to the Monte Carlo tree search Ms Pac-Man agent I [worked on](https://github.com/stewartml/montecarlo-pacman) for a summer research internship.  The original agent performs reasonably well when the model of enemy behaviour used in the Monte Carlo tree search matches the actual behaviour of the ghost, but performs a lot worse when the model does not match the actual behaviour; my task is to attempt to use machine learning to learn the model.

License
--------

My code is licensed under the MIT License (see LICENSE for details), where my code is the Java files under the 'agent' directory.  I'm making this available in the hope that it benefits the field; while not required to, if you make any changes to the software it would be great if you got in touch to tell me what you've done.

All rights are reserved for all files under the 'thesis' directory.  Do not publish, copy, distribute or modify them in any way.

The agent code makes use of the [Ms Pac-Man vs Ghosts League](http://www.pacman-vs-ghosts.net/) framework, which is bundled as PacManVsGhosts6.2.jar in the 'agent' directory: please see the 'copyright.txt' file in the jar for the associated license.  The agent/data directory is part of the framework and has the same license.

The code under the 'ml' directory is from the above-mentioned machine learning class with my solutions added.
