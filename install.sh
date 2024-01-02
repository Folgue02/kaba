#!/bin/bash
REGULAR_USER=$(whoami)
TARGET_FOLDER="/opt/kaba"
DIST_PATH="app/build/install/app"
INSTALLED_EXE_PATH="/bin/kaba"

function yesNo() {
    PROMPT="Are you sure?(Y/N) "
    if [[ -z $1 ]]; then
        PROMPT=$1
    fi

        
    while [ 1 ]; do
        read -p "$PROMPT" CHOICE

        case $CHOICE in
            [yY])
                return 1
                ;;
            [nN])
                return 0
                ;;
            *)
                echo "Only y/Y and n/N are accepted."
                ;;
        esac
    done
}

function die() {
    CODE=1
    if [ -z $2 ]; then
        CODE=$2
    fi

    echo $1
    exit $CODE
}

function installKaba() {
    echo "Compiling and creating installation..."
    ./gradlew installDist
    if [ $? -ne 0 ]; then
        die 'Something went wrong while compiling Kaba.' 1
    fi

    echo "Creating installation folder ($TARGET_FOLDER)..."
    sudo mkdir $TARGET_FOLDER
    if [ $? -ne 0 ]; then
        die 'Something went wrong while creating the installation folder.' 1
    fi

    sudo chown $REGULAR_USER:$REGULAR_USER $TARGET_FOLDER -R
    if [ $? -ne 0 ]; then
        die 'Something went wrong while changing the permissions of the installation folder.' 1
    fi

    echo "Copying files..."
    cp -rv $DIST_PATH/* $TARGET_FOLDER
    if [ $? -ne 0 ]; then
        die 'Something went wrong while copying the files of Kaba.' 1
    fi
    
    echo "Creating executable link ($INSTALLED_EXE_PATH)"
    sudo ln -s $TARGET_FOLDER/bin/app $INSTALLED_EXE_PATH
    if [ $? -ne 0 ]; then
        die 'Something went wrong while creating the executable link.' 1
    fi

    echo "=> Done."
    kaba -v
}

function removePreviousInstallation() {
    echo "Removing the previous installation..."
    sudo rm -rf $TARGET_FOLDER
    if [ $? -ne 0 ]; then
        die 'Something went wrong while removing the previous Kaba installation folder.' 1
    fi

    if [[ ! -e $INSTALLED_EXE_PATH ]]; then
        echo "Removing the previous installation's executable link..."
        sudo rm $INSTALLED_EXE_PATH -rf
        if [ $? -ne 0 ]; then
            die 'Something went wrong while removing the previous Kaba executable linking.' 1
        fi
    fi

}

if [[ $REGULAR_USER = "root" ]]; then
    echo "This script cannot be executed as root"
    exit 1
fi

if [[ -e $TARGET_FOLDER ]]; then
    echo "A previous installation of Kaba was found."
    yesNo "Do you want to remove it? (if not, the installation won't continue) "
    if [ $? -eq 1 ]; then
        removePreviousInstallation
    else
        echo "Quitting the installer." 
        exit 2
    fi

    installKaba
fi
