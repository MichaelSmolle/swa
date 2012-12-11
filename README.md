swa
===

Lab 2 (2012)

initialize locat repo:

# don't ferget to change -Dversion=#Number
mvn install:install-file -DlocalRepositoryPath=repo -DcreateChecksum=true -Dpackaging=jar -Dfile=fingerprint.jar -DgroupId=org.tuwien.swalab2 -DartifactId=swazam-parent -Dversion=1.0
