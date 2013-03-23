
/*
Non-learning ghost model against the Legacy controller.
*/

nodeExpansionThreshold = 30;
maximumSimulationLength = 250;
pacManModel = new RandomNonRevPacMan();
ghostModel = new Legacy();
selectionPolicy = new LevineUcbSelectionPolicy(4000);
opponent = new Legacy();

