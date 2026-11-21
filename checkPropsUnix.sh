. ./deploy.properties
. $HOME/Documents/panopset/dev.properties

echo "checking variables..."
echo "FXV = $FXV"
echo "SITE_NAME = $SITE_NAME"

if [ "$SITE_NAME" = "" ]; then
 echo '$SITE_NAME is blank, please define required environment variables, see documentation in docs/setup.md.'
 exit 2
fi

if [ "$SITE_USR" = "" ]; then
 echo '$SITE_USR is blank, please define required environment variables, see documentation in docs/setup.md.'
 exit 2
fi

if [ "$SITE_PWD" = "" ]; then
 echo '$SITE_PWD is blank, please define required environment variables, see documentation in docs/setup.md.'
 exit 2
fi

if [ "$SITE_DN" = "" ]; then
 echo '$SITE_DN is blank, please define required environment variables, see documentation in docs/setup.md.'
 exit 2
fi

if [ "$INSTALLER_PFX" = "" ]; then
 echo '$INSTALLER_PFX is blank, something went wrong with loading platform specific properties, exiting script.'
 exit 2
fi

echo SITE_DN is $SITE_DN
echo Panopset version is ${PV}
export INSTALLER="${INSTALLER_PFX}${PV}${INSTALLER_SFX}"
export PCI_JSON="pci_${INSTALLER}.json"
export TGT_HTML=${SITE_USR}@${SITE_NAME}:/var/www/${SITE_DN}/html

