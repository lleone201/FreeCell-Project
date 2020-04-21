JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Card.java \
	TestCard.java \
	Deck.java \
	TestDeck.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
