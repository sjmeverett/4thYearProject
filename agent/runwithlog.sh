#!/bin/bash
cd "$(dirname "$0")"
./run.sh 2> >(tee -a /home/student/c/cxb07142/PC/4th\ Year/Project/agent/error.log >&2)
