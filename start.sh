#!/bin/bash

docker-compose up -d

docker-compose scale slave=3

docker-compose exec client bash run-programs.sh
