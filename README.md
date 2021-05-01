# Medve-Euler

Its name *Medve* refers to the organisation [Matematika Összeköt Egyesület](https://medvematek.hu/). 
Euler refers to the famous mathematician [Leonhard Euler](https://en.wikipedia.org/wiki/Leonhard_Euler).

## Aim

The aim of this project is to generate an Euler circle in a given connected graph. 
Meaning, to find such path in the graph, which covers all of its edges exactly once.

## Limitations

+ The program can only work with connected graphs.

+ It can generate Euler circles only for graphs that doesn't contain vertices with odd degree. 
  It is planned to make the program work on these kinds of graphs, using edge duplications.

## Requirements

+ To build the application you need [Maven](https://maven.apache.org/download.cgi).
+ To run the application you need [Java 11](https://adoptopenjdk.net/).

## Build

Use `mvn clean install`, to generate the runnable jar file. 
After a successful build, it can be located inside the `target` folder. 
There will be two jar files, the one with suffix *-jar-with-dependencies* will be the runnable jar.

## Execution

You can run the application the following way, where

+ `medve-euler-1.0.jar` is the runnable jar file, containing dependencies. In this example it is placed in the root
  folder of the project.
+ `-in .\src\main\resources\graphs\graph1.txt` tells the program, where to look for the input graph.
+ `-out .\src\main\resources\graphs\graph1_out.csv` tells the program, where to write the result.
+ `-s 1` tells the program the ID of the starting vertex in the graph.

`java -jar medve-euler-1.0.jar -in .\src\main\resources\graphs\graph1.txt -out .\src\main\resources\graphs\graph1_out.csv -s 1`

To get help with the command line arguments, use the `-h` or the `--help` flag.

## References

I based my program on the algorithm, and solution from this [site](https://www.geeksforgeeks.org/fleurys-algorithm-for-printing-eulerian-path/).

### Useful resources

[https://courses.lumenlearning.com/wmopen-mathforliberalarts/chapter/introduction-euler-paths/](https://courses.lumenlearning.com/wmopen-mathforliberalarts/chapter/introduction-euler-paths/)

[https://web.mit.edu/urban_or_book/www/book/chapter6/6.4.4.html](https://web.mit.edu/urban_or_book/www/book/chapter6/6.4.4.html)