#!/bin/sh

set -x

java -Djava.security.policy=servidor.permisos -cp .:afs_servidor.jar ServidorAFS $*
