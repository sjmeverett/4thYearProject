#!/bin/bash
for i in {13..20}
do
    ssh telford-$i screen -d -m "/home/student/c/cxb07142/PC/4th\ Year/Project/agent/run.sh > /home/student/c/cxb07142/PC/4th\ Year/Project/agent/runs/telford-$i.txt"
done

