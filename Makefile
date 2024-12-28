.PHONY: all clean package test run

default: all

all: clean package test

clean:
	mvn clean

package:
	mvn package

test:
	mvn test

run:
	mvn exec:java
