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

[source,ruby]
----
require 'asciidoctor'  # <1>

puts Asciidoctor.render_file('sample.adoc', :header_footer => true)  # <2>
----
<1> Imports the library
<2> Reads, parses and renders the file

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


