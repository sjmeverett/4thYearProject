#!/bin/bash
cd "$(dirname "$0")"
java -cp ./bin:./PacManVsGhosts6.2.jar:./gson-2.2.2.jar pacman.experimentclient.ExperimentClient $1 $2
