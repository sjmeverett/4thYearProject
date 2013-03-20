
/*
Learning ghost model against the Legacy2TheReckoning controller, using 5 iterations of learning and pretrained weights.
*/

nodeExpansionThreshold = 30;
maximumSimulationLength = 100000;
pacManModel = new RandomNonRevPacMan();
ghostModel = new NeuralNetworkGhostController(new RouletteMoveSelectionStrategy(), 5, true);
tasks = [ ghostModel ];
selectionPolicy = new LevineUcbSelectionPolicy(4000);
opponent = new Legacy2TheReckoning();

