# WSPD-fast-geometric-algorithms

> O(n log n) approximation algorithms for 3D geometric problems: closest and farthest pairs in a point cloud, aesthetically pleasing 2D drawings of 3D graphs. The asymptotic complexity is made possible by first computing a Well-Separated Pair Decomposition (WSPD) of the input point cloud.

## Table of contents
  * [Description](#description)
  * [Running](#running)
  * [Authors](#authors)
  * [References](#references)

## Description

This code implements O(n log n) algorithms to solve the following geometric problems: 
* computing the **closest pair** of points (exactly) in a 3D point cloud
* computing the **farthest pair of points** (approximately) in a 3D point cloud
* producing aesthetically pleasing **2D drawings of 3D graphs** (by approximating the [Fruchterman and Reingold, 1991](https://dcc.fceia.unr.edu.ar/sites/default/files/uploads/materias/fruchterman.pdf) force-directed graph drawing method)

First, a [Well-Separated Pair Decomposition (WSPD)](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.94.9691&rep=rep1&type=pdf) (class `WSPD`) of the input point cloud is computed. This is done by building an octree storing the point cloud (classes `Octree` and `OctreeNode`).

Below is an example in 2 dimensions. A quadtree (the 2D equivalent of the octree) (*i, iv*) is constructed and used to build a WSPD (*ii, iii, v*).

<p align="center"><img src="/img/quadtree_and_wspd.png"/></p>

The WSPD is then used to determine the **closest pair** of points (class `FastClosestPair`, to be compared with the naive implementation `SlowClosestPair`).

Similarly, it allows to find a pair of points giving an **approximation of the diameter** of the point cloud (class `FastDiameter`, to be compared with `SlowDiameter`).

Both cases are tested in the class `PointCloudResults`.

Finally, the WSPD has been proposed ([Fabian Lipp, Alexander Wolff, and Johannes Zink, 2016](https://www.mdpi.com/1999-4893/9/3/53)) as a method to draw unweighted 3D graphs in 2D. The general idea (without WSPD) ([Fruchterman and Reingold, 1991](https://dcc.fceia.unr.edu.ar/sites/default/files/uploads/materias/fruchterman.pdf)) is to introduce:
* attractive forces between neighbouring vertices
* repulsive forces between all pairs of vertices

Starting from a random drawing and then iteratively moving each vertex according to the forces exerted on it yields a balanced and clear drawing of the graph. The WSPD helps speed up the computation of the repulsive forces.

This is done in class `FastFR91Layout` (to be compared with `FR91Layout`). *FR91* stands for Fruchterman & Reingold 1991.

`DrawGraph` and `NetworkLayout` allow to visualize the results, iteration after iteration. For instance, with the `ash85.mtx` graph, we have after 0 iterations, 150 iterations of the naive implementation, and 150 iterations of the WSPD-based approximation:

<p align="center"><img src="/img/ash85.png"/></p>

For performance comparisons, the class `GraphDrawingResults` performs a fixed number of iterations of both methods and compares their execution times.

The `Main` class is used to perform various small tests of the algorithms.

## Running

Sample outputs are provided in `results/outputs`.

First download or clone this repository. Then `cd` to the `src` directory, and:

* To execute the `Main` class (various tests), run:

```
javac -cp .:./lib/* Main.java
java -ea -cp .:./lib/* Main
```

* To execute the closest and farthest pair computations on file `data/pointclouds/x.off`:

```
javac -cp .:./lib/* PointCloudResults.java
java -ea -cp .:./lib/* PointCloudResults /FULL/PATH/TO/REPO/data/pointclouds/x.off
```

* To display the results of the graph drawing procedures step by step, with input graph `data/networks/x.mtx`:

```
javac -cp .:./lib/* NetworkLayout.java
java -ea -cp .:./lib/* NetworkLayout /FULL/PATH/TO/REPO/data/networks/x.mtx
```

* To only perform the graph drawing computation without any display (for performance comparisons), with input graph `data/networks/x.mtx`:

```
javac -cp .:./lib/* GraphDrawingResults.java
java -ea -cp .:./lib/* GraphDrawingResults /FULL/PATH/TO/REPO/data/networks/x.mtx
```

## Authors

Authors of the main implementations (Octree, WSPD, fast closest pair, fast diameter, fast graph drawing): [Timothée Chauvin](https://github.com/TimotheeChauvin) and [JS Denain](https://github.com/denainjs).

The naive (O(n<sup>2</sup>)) implementations, the skeletons of some classes, as well as some code dealing with input/output and graphs (`src/jdg`), were provided by [Luca Castelli Aleardi](http://www.lix.polytechnique.fr/~amturing/) (these classes are preceded by the line `@author Luca Castelli Aleardi`).

## References

* [The Well-Separated Pair Decomposition and Its Applications](http://people.scs.carleton.ca/~michiel/aa-handbook.pdf) (Michiel Smid, 2016)
* [Well-Separated Pairs Decomposition](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.94.9691&rep=rep1&type=pdf) (Sariel Har-Peled, 2008)
* [Graph Drawing by Force-Directed Placement](https://dcc.fceia.unr.edu.ar/sites/default/files/uploads/materias/fruchterman.pdf) (Fruchterman and Reingold, 1991)
* [Faster Force-Directed Graph Drawing with the Well-Separated Pair Decomposition](https://www.mdpi.com/1999-4893/9/3/53) (Fabian Lipp, Alexander Wolff, and Johannes Zink, 2016)