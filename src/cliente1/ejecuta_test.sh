#!/bin/sh

set -x
java -Djava.security.policy=cliente.permisos -cp .:afs_cliente.jar Test $*

