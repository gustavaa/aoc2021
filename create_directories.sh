#!/usr/bin/env bash

for i in $(seq -w 2 25); do
  echo $i
  mkdir "day$i"
  cp -a day01/. "day$i"/
  
done