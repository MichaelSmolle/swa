##########################
# swa                    #
##########################

#Lab 2 (2012)

Before you do someting, IMPORT fingerprint.jar

#go to importMe
cd importMe

mvn install:install-file -Dfile=importMe/fingerprint.jar -DgroupId=ac.at.tuwien.infosys.swa.audio -DartifactId=fingerprint -Dversion=0.1.1 -Dpackaging=jar

