
set version=%1

echo "Unzip rescueapp-v"%version%".tgz"

mkdir rescueapp-v%version%
tar -xvzf rescueapp-v%version%.tgz -C rescueapp-v%version%

echo "Removing rescueapp-v"%version%".tgz"
del rescueapp-v%version%.tgz

echo "Compressing rescueapp-v"%version%
tar -czvf rescueapp-v%version%.tgz rescueapp-v%version%

echo "Copying generated package in frontend project"
copy rescueapp-v%version%.tgz ..\..\rescueapp.fe\local-packages\

echo "Success -> rescueapp-v"%version%".tgz created"
