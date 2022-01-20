#!/usr/bin/env bash

# ================================================================================================
# A shell script to create a git tag.
# How to use: ./createTag.sh <tag>

tag=$1

if [ -z "$tag" ]; then
    echo "Invalid input. Usage: ./createTag.sh <branch> <tag> . Try again"
    exit 1
fi

git tag $tag
git push origin $tag
