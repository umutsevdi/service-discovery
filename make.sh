#!/bin/bash
service_discovery=$(dirname -- $(readlink -fn -- "$0"))
clientdir=$service_discovery/client
serverdir=$service_discovery/server
servicedir=$service_discovery/service
# Client
echo "Compiling client"
cd $clientdir 
rm -rf $clientdir/out/
(javac $clientdir/src/*.java $clientdir/src/*/*.java -d $clientdir/out)
cp $clientdir/Manifest.txt $clientdir/out/
cd $clientdir/out
(jar -cfvm $service_discovery/out/client.jar $clientdir/out/Manifest.txt $clientdir/out/*.class $clientdir/out/*/*.class)
# Server
echo "Compiling server"
cd $serverdir
rm -rf $serverdir/out/
(javac $serverdir/src/*.java $serverdir/src/*/*.java -d $serverdir/out)
cp $serverdir/Manifest.txt $serverdir/out/
cd $serverdir/out
(jar -cfvm $service_discovery/out/server.jar $serverdir/out/Manifest.txt $serverdir/out/*.class $serverdir/out/*/*.class)
# Service
echo "Compiling service"
cd $servicedir
rm -rf $servicedir/out/
`javac $servicedir/src/*.java $servicedir/src/*/*.java -d $servicedir/out`
cp $servicedir/Manifest.txt $servicedir/out/
cd $servicedir/out
(jar -cfvm $service_discovery/out/service.jar $servicedir/out/Manifest.txt $servicedir/out/*.class $servicedir/out/*/*.class)
