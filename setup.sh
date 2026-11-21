#!/bin/bash
. ./checkenv.sh
git config --global user.name "$PAN_NM"
git config --global user.email $PAN_EMAIL
sudo apt install git gitk vim build-essential cmake python-is-python3 ninja-build openssh-client openssh-server
