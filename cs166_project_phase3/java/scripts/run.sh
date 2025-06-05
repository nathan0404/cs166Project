#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
ROOT="$DIR/.."

java -cp "$ROOT/bin:$ROOT/lib/lanterna-3.1.3.jar:$ROOT/lib/pg73jdbc3.jar" \
     AirlineManagement "$@"
