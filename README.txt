##########################
# swa                    #
##########################

#Lab 2 (2012)




In order to deploy the webapp you need JDK7 and Glassfish 3.1.2.2
Start glassfish from C:\glassfish-3.1.2.2\glassfish\bin  with startsrv.bat domain1
navigate to localhost:4848
under applications hit deploy and select the war file from \swa\swazam-webapp\target  change the name to swazam-webapp under contxt-root this is important otherwise the rest api wont work
hit ok
Under resources/ JDBC/JDBC Resources make sure the jdbc/__default is present otherwise create with derby connection pool
profit



#Peer
In order to start the peer you will need a peer.properties file.
A sample configuration is included in swa/swazam-peer. All properties except
"bindToIp" are necessary.
If bindToIp is not specified the peer will search for the first none virtual
network intface which is online and use it's first assigned IP address.
To start the client use the following command:
java -jar swazam-peer-1.0-jar-with-dependencies.jar PORT UNIQUE_ID
where port is used to listen for new peer connections and UNIQUE_ID is a
string.
The peer will open a second port (PORT+1) to listen for incoming client
requests, keep that in mind while starting mutliple peers.
