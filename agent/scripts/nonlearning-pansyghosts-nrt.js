
/*
Non-learning ghost model against the PansyGhosts controller.
*/

nodeExpansionThreshold = 30;
maximumSimulationLength = 250;
simulationCount = 1000;
pacManModel = new RandomNonRevPacMan();
ghostModel = new Legacy();
selectionPolicy = new LevineUcbSelectionPolicy(4000);
opponent = new PansyGhosts();

