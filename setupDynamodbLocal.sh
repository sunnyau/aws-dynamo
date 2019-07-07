#!/bin/bash

# To download dynamodb_local_latest.tar.gz file to src/lib directory
# curl https://s3-us-west-2.amazonaws.com/dynamodb-local/dynamodb_local_latest.tar.gz -o ./src/lib/dynamodb_local_latest.tar.gz

mkdir ./tmp
tar -xvf ./src/lib/dynamodb_local_latest.tar.gz -C ./tmp