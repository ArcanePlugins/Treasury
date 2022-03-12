#!/usr/bin/env bash

# ================================================================================================
# A shell script to update the maven version
# This updates the "version" tag in the parent pom, and updates the
# plugin.version and plugin.specificationVersion properties , also cleaning up the
# "pom.xml.versionsBackup" files leftover the maven commands
# ================================================================================================
# How to use: if you just want to change the version, plainly run "./updateVersion.sh" in the
# terminal and it will tell you to specify a new version.
# If you want to change the version and commit, you have to run "./updateVersion.sh commit" in
# the terminal and it will again tell you to specify a new version.
# If you want to change the version, commit and push, you have to run "./updateVersion.sh commit
# push" in the terminal and it will again tell you to specify a new version.
# ================================================================================================

function commit {
    version=$1
    push=$2
    git add "pom.xml"
    git add "api/pom.xml"
    git add "core/pom.xml"
    SCRIPT_DIR=$(dirname $0)

    for PLATFORM in $SCRIPT_DIR/platform/*/; do
        pom_in_dir=${PLATFORM}pom.xml
        if [ -f $pom_in_dir ]; then
            git add $pom_in_dir
        fi
        for MODULE in ${PLATFORM}*/; do
            POM_FILE=${MODULE}pom.xml

            if [[ -f $POM_FILE ]]; then
                git add $POM_FILE
            fi
        done
    done
    git commit -m "(updateVersion.sh) Bump version to ${version}"
    if [ -n "$push" ] && [ "$push" = "push" ]; then
        git push
    fi
}

read -p "Enter new version: " input
IFS='-'

read -a array <<<"$input"
arrayLen="${#array[@]}"

# Sets the maven versions
mvn versions:set -DnewVersion="${input}"
mvn versions:commit

# Updates the properties
if [ "$arrayLen" -eq 1 ]; then
    mvn versions:set-property -Dproperty=plugin.version -DnewVersion="${array[0]}"
    mvn versions:set-property -Dproperty=plugin.specificationVersion -DnewVersion="RELEASE"
else
    mvn versions:set-property -Dproperty=plugin.version -DnewVersion="${array[0]}"
    mvn versions:set-property -Dproperty=plugin.specificationVersion -DnewVersion="${array[1]}"
fi

# Remove the pom.xml.versionsBackup file of the parent pom, because we don't need it
rm -rf pom.xml.versionsBackup

echo "Version changed successfully to ${input}"

if [ -n "$1" ] && [ "$1" = "commit" ]; then
    commit "$input" "$2"
fi
