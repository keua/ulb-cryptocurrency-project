## Maser node

java -jar master-node.jar [listening-port] [host]  
`java -jar master-node.jar 3333 localhost`

## Relay 1
java -jar relay-node.jar [host] [listening-port] [master-port]  
`java -jar relay-node.jar localhost 2222 3333`

## Relay 2
java -jar relay-node.jar [host] [listening-port] [master-port]  
`java -jar relay-node.jar localhost 2223 3333`

## Miner 1

java -jar miner.jar [host] [relay1-port] [relay2-port]  
`java -jar miner.jar localhost 2222 2223`

## Miner 2

java -jar miner.jar [host] [relay1-port] [relay2-port]  
`java -jar miner.jar localhost 2223 2222`

## Wallet 1

java -jar wallet.jar [host] [relay1-port] [relay2-port]  
`java -jar wallet.jar localhost 2222 2223`

## Wallet 2

java -jar wallet.jar [host] [relay1-port] [relay2-port]  
`java -jar wallet.jar localhost 2223 2222`