CLASSPATH := lib/jackson-core-2.0.4.jar:lib/jackson-databind-2.0.4.jar

ifneq ($(CLASSPATH),)
        classpath:=  $(CLASSPATH):$(classpath)
endif

export CLASSPATH:=$(classpath)

JFLAGS = -g
JC = javac
RUN = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

default: classes

classes:
	javac -cp ${classpath} src/hackathon/boxme/*.java

clean: 
	rm src/hackathon/boxme/*.class
