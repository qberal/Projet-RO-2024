compil: main.jar

mainRO.class:src/graphro/*java
	javac --source-path ./src --class-path ./classes -d ./classes ./src/graphro/mainRO.java

main.jar: classes/graphro/mainRO.class
	jar cfe main.jar graphro.mainRO -C ./classes graphro

run: main.jar
	java -jar main.jar
