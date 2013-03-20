#!/bin/bash
for i in {13..20}
do
    screen -d -m ssh telford-$i "/home/student/c/cxb07142/PC/4th\ Year/Project/agent/run.sh | tee /home/student/c/cxb07142/PC/4th\ Year/Project/agent/runs/telford-$i-1.txt"
    screen -d -m ssh telford-$i "/home/student/c/cxb07142/PC/4th\ Year/Project/agent/run.sh | tee /home/student/c/cxb07142/PC/4th\ Year/Project/agent/runs/telford-$i-2.txt"
done

