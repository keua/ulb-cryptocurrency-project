#old

## Relay node
mvn 
-Dexec.args="-classpath %classpath com.ulb.cryptography.network.RelayServer" 
-Dexec.executable="java" 
-Dexec.classpathScope=runtime 
-Dmaven.ext.class.path=/mnt/c/Program\ Files/NetBeans\ 8.2/java/maven-nblib/netbeans-eventspy.jar
-Dfile.encoding=UTF-8 org.codehaus.mojo:exec-maven-plugin:1.2.1:exec 

mvn -Dexec.args="-classpath %classpath com.ulb.cryptography.network.RelayServer 2145 3333 localhost" -Dexec.executable="java" -Dexec.classpathScope=runtime -Dmaven.ext.class.path=/mnt/c/Program\ Files/NetBeans\ 8.2/java/maven-nblib/netbeans-eventspy.jar -Dfile.encoding=UTF-8 org.codehaus.mojo:exec-maven-plugin:1.2.1:exec 

## Maser node
mvn 
-Dexec.args="-classpath %classpath com.ulb.cryptography.network.MasterNodeServer" 
-Dexec.executable="java" 
-Dexec.classpathScope=runtime 
-Dmaven.ext.class.path=/mnt/c/Program\ Files/NetBeans\ 8.2/java/maven-nblib/netbeans-eventspy.jar
-Dfile.encoding=UTF-8 org.codehaus.mojo:exec-maven-plugin:1.2.1:exec 

mvn -Dexec.args="-classpath %classpath com.ulb.cryptography.network.MasterNodeServer" -Dexec.executable="java" -Dexec.classpathScope=runtime -Dmaven.ext.class.path=/mnt/c/Program\ Files/NetBeans\ 8.2/java/maven-nblib/netbeans-eventspy.jar -Dfile.encoding=UTF-8 org.codehaus.mojo:exec-maven-plugin:1.2.1:exec 


## Wallet
mvn -Dexec.args="-classpath %classpath com.ulb.cryptography.network.WalletClient localhost 2145" -Dexec.executable="java" -Dexec.classpathScope=runtime -Dmaven.ext.class.path=/mnt/c/Program\ Files/NetBeans\ 8.2/java/maven-nblib/netbeans-eventspy.jar -Dfile.encoding=UTF-8 org.codehaus.mojo:exec-maven-plugin:1.2.1:exec 

## Miner

mvn -Dexec.args="-classpath %classpath com.ulb.cryptography.network.MinerClient localhost 2145" -Dexec.executable="java" -Dexec.classpathScope=runtime -Dmaven.ext.class.path=/mnt/c/Program\ Files/NetBeans\ 8.2/java/maven-nblib/netbeans-eventspy.jar -Dfile.encoding=UTF-8 org.codehaus.mojo:exec-maven-plugin:1.2.1:exec 

# new

## Maser node
mvn 
-Dexec.args="-classpath %classpath com.ulb.cryptography.network.MasterNodeServer" 
-Dexec.executable="java" 
-Dexec.classpathScope=runtime 
-Dfile.encoding=UTF-8 org.codehaus.mojo:exec-maven-plugin:1.2.1:exec 

mvn -Dexec.args="-classpath %classpath com.ulb.cryptography.network.MasterNodeServer" -Dexec.executable="java" -Dexec.classpathScope=runtime -Dfile.encoding=UTF-8 org.codehaus.mojo:exec-maven-plugin:1.2.1:exec 