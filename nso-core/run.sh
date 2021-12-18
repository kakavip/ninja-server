mvn install

export `cat local.env| xargs`
java -server -Xms1024M -Xmx8192M -XX:MaxHeapFreeRatio=50 -jar  target/NinjaServer.jar

