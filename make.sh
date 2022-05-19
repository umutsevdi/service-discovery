#!/bin/bash
cd client/
rm -rf out/
mkdir out
for i in $(ls src); do 
    if[[ -d $i ]]; then
        dir+="$i "
    fi
done 
javac 

