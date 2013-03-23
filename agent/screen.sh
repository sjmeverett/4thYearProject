#!/bin/bash
for i in {13..20}
do
    screen -d -m ssh telford-$i "/home/student/c/cxb07142/PC/4th\ Year/Project/agent/run.sh 2> >(tee -a /home/student/c/cxb07142/PC/4th\ Year/Project/agent/error.log >&2) "
done

