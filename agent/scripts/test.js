
nodeExpansionThreshold = 30;
maximumSimulationLength = -1;
deathPenalty = 10000;
scaleDeathPenalty = false;
completionReward = 10000;
pacManModel = new RandomNonRevPacMan();
neuralNetwork = new NeuralNetworkGhostController(new RouletteMoveSelectionStrategy(), 5);
ghostModel = neuralNetwork;
selectionPolicy = new LevineUcbSelectionPolicy(4000);
discardTreeOnDecision = true;
opponent = new Legacy();
simulationCount = -1;
useGhostPositions = false;
eatGhostNode = false;

