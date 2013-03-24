
/*
Non-learning ghost model against the RandomGhosts controller.
*/

nodeExpansionThreshold = 30;
maximumSimulationLength = 100000;
pacManModel = new RandomNonRevPacMan();
ghostModel = new RandomGhosts();
selectionPolicy = new LevineUcbSelectionPolicy(4000);
opponent = new RandomGhosts();

