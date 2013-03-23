#!/bin/bash
cd "$(dirname "$0")"
ssh telford-$1 ./run.sh > >(tee -a run.log) 2> >(tee -a error.log >&2)
