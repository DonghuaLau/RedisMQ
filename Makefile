
classpath=".:./bin:lib/commons-pool2-2.4.2.jar:lib/jedis-2.9.0.jar:lib/commons-lang3-3.5.jar"
TARGET=RedisMQ
SRC_DIR=src
#SRC=${SRC_DIR}/PrintListener.java ${SRC_DIR}/PubClient.java ${SRC_DIR}/SubClient.java ${SRC_DIR}/Constants.java ${SRC_DIR}/RedisMQ.java
SRC=${SRC_DIR}/*.java
DEST=bin

all:
	javac -cp ${classpath} ${SRC} -d ${DEST}

run:
	@java -cp ${classpath} ${TARGET}

clean:
	rm -rf bin/*.class


