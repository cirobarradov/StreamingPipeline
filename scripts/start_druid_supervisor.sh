#!/bin/sh

curl -XPOST -H'Content-Type: application/json' -d @$1 http://localhost:8090/druid/indexer/v1/supervisor
