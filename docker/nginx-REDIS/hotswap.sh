#!bin/bash

if grep -q "$1" '/etc/nginx/nginx.conf'; then
  echo Warning: already pointing to "$1" host # SomeString was found
else 
  sed -i "/6379/s/.*/    server ${1}:6379;/" /etc/nginx/nginx.conf     # matched lines -> then remove
  /usr/sbin/nginx -s reload
fi
