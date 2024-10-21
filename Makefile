compil: mainRO.class

mainRO.class:src/graphro/*java
	javac --source-path ./src --class-path ./classes -d ./classes ./src/graphro/mainRO.java

run: classes/graphro/mainRO.class
	java -classpath ./classes/ graphro.mainRO