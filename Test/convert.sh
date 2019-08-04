#!/bin/bash
echo $1
echo $2
ffmpeg -i "$1" "$2"