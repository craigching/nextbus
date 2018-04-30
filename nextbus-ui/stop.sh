#!/bin/sh

nginx -p `pwd`/ -c conf/nginx.conf -s stop
