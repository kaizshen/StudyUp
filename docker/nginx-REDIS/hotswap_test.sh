#!bin/bash

if grep -q "$1" 'test.txt'; then
  echo Warning: already pointing to "$1" host # SomeString was found
else 
  sed -i "/6379/s/.*/    server ${1}:6379;/" test.txt     # matched lines -> then remove
  echo reload ...
fi

