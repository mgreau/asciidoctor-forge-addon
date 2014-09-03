= Example Manual
${docWriter} <doc.writer@example.org>
v1.0, 2014-08-18
:example-caption!:
ifndef::imagesdir[:imagesdir: images]
ifndef::sourcedir[:sourcedir: ../java]

This is a user manual for an example project.

== Introduction

//[yell]
This project does something.
We just haven't decided what that is yet.

== Source Code

[source,java]
.Java code from project
----
include::{sourcedir}/example/StringUtils.java[tags=contains,indent=0]
----

 $ mvn process-resources

== Images

[.thumb]
image::sunset.jpg[]

[ditaa,asciidoctor-diagram-process]
....
                   +-------------+
                   | Asciidoctor |-------+
                   |   diagram   |       |
                   +-------------+       | PNG out
                       ^                 |
                       | ditaa in        |
                       |                 v
 +--------+   +--------+----+    /---------------\
 |        |---+ Asciidoctor +--->|               |
 |  Text  |   +-------------+    |   Beautiful   |
 |Document|   |   !magic!   |    |    Output     |
 |     {d}|   |             |    |               |
 +---+----+   +-------------+    \---------------/
     :                                   ^
     |          Lots of work             |
     +-----------------------------------+
....

== Attributes

.Built-in
asciidoctor-version:: {asciidoctor-version}
safe-mode-name:: {safe-mode-name}
docdir:: {docdir}
docfile:: {docfile}
imagesdir:: {imagesdir}
//projectdir:: {projectdir}
//rootdir:: {rootdir}

.Custom
sourcedir:: {sourcedir}
Publish URL:: {publish-url}

== Includes

.include::subdir/_b.adoc[]
====
include::subdir/_b.adoc[]
====

WARNING: Includes can be tricky!
