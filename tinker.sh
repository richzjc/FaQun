#!/bin/bash

cd Aphrodite
if grep "tinker-support" build.gradle
then
    echo "already exists"
else
    echo -e "\napply from: 'tinker-support.gradle'" >> build.gradle
fi
cd ..
cd Hephaistos
if grep "tinker-support" build.gradle
then
    echo "already exists"
else
    echo -e "\napply from: 'tinker-support.gradle'" >> build.gradle
fi