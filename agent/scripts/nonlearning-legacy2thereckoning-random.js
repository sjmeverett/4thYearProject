
/*
Non-learning ghost model against the Legacy2TheReckoning controller.
*/

nodeExpansionThreshold = 30;
maximumSimulationLength = 100000;
pacManModel = new RandomNonRevPacMan();
ghostModel = new RandomGhosts();
selectionPolicy = new LevineUcbSelectionPolicy(4000);
opponent = new Legacy2TheReckoning();

