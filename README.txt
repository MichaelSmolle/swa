##########################
# swa                    #
##########################

#Lab 2 (2012)




In order to deploy the webapp you need JDK7 and Glassfish 3.1.2.2
Start glassfish from C:\glassfish-3.1.2.2\glassfish\bin  with startsrv.bat domain1
navigate to localhost:4848
under applications hit deploy and select the war file from \swa\swazam-webapp\target  change the name to swazam-webapp this is important otherwise the rest api wont work
hit ok
Under resources/ JDBC/JDBC Resources make sure the jdbc/__default is present otherwise create with derby connection pool
profit