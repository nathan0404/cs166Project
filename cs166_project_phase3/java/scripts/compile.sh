#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# This gets us to the project root, one level above "java"
ROOT="$DIR/.."

# Make sure bin folder exists
mkdir -p "$ROOT/bin"

# Compile
javac -cp "$ROOT/lib/lanterna-3.1.3.jar:$ROOT/lib/pg73jdbc3.jar:$ROOT/src" \
      -d "$ROOT/bin" "$ROOT/src/"*.java
