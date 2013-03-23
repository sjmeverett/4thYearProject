#!/bin/bash
for i in {14..25}
do
    screen -d -m ssh telford-$i "/home/student/c/cxb07142/PC/4th\ Year/Project/agent/runwithlog.sh"
done

