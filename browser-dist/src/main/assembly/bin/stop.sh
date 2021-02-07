#!/usr/bin/env bash

base_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
base_dir=${base_dir}/..

# echo base_dir:${base_dir}

pid_file=${base_dir}/pid
cat ${pid_file} | awk '{cmd="kill "$1; print(cmd); system(cmd);}'
rm -rf ${pid_file}
