SCALAC = scalac -feature

LATEX = src/main/scala/latex/*.scala
HTML = src/main/scala/html/*.scala
GRAPHVIZ = src/main/scala/graphviz/*.scala
UTIL = src/main/scala/util/*.scala
OUTLINES = src/main/scala/outlines/*.scala
SOURCES = ${UTIL} ${GRAPHVIZ} ${HTML} ${LATEX} ${OUTLINES}

SOURCEPATH = src

jmutils.jar: ${SOURCES}
	${SCALAC} -d $@ $^ -sourcepath ${SOURCEPATH}

doc: .doc-build
.doc-build: ${SOURCES} src/root.scaladoc
	mkdir -p doc
	scaladoc -diagrams -diagrams-dot-path /usr/bin/dot \
		-classpath out:${TESTLIB} \
		-doc-external-doc:/usr/share/scala-2.11/lib/scala-library.jar#file:///usr/share/doc/scala-2.11/api/library \
		-doc-title "Maraist Utils" \
		-doc-root-content src/root.scaladoc -d doc \
		-sourcepath ${SOURCEPATH} ${SOURCES}
	touch $@

clean:
	rm -f jmutils.jar .doc-build
