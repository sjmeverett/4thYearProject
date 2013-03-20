#!/bin/bash
for i in {13..21}
do
    ssh telford-$i screen -d -m "/home/student/c/cxb07142/PC/4th\ Year/Project/agent/run.sh"
done

