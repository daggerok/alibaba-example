#!/usr/bin/env bash
./mvnw release:clean \
       release:prepare \
       release:perform \
         --batch-mode \
         -DgenerateBackupPoms=false
