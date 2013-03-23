#!/bin/bash
cd "$(dirname "$0")"
ssh telford-$1 /home/student/c/cxb07142/PC/4th\ Year/Project/agent/run.sh > >(tee -a run.log) 2> >(tee -a error.log >&2)
