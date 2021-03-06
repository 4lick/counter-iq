#!/bin/sh -e
DEVICE=$1
[ "$DEVICE" = "" ] && DEVICE=fr920xt

DEVELOPER_KEY=$2
[ "$DEVELOPER_KEY" = "" ] && DEVELOPER_KEY=/Path/to/key.der

RESOURCE_PATH=$(find . -path './resources*.xml' | xargs | tr ' ' ':')
monkeyc -o bin/counter-widget.prg -d $DEVICE -m manifest.xml -z $RESOURCE_PATH source/*.mc -w -y $DEVELOPER_KEY
