
name := "mnist"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"

// https://mvnrepository.com/artifact/org.jfree/jfreechart
libraryDependencies += "org.jfree" % "jfreechart" % "1.5.0"

// https://mvnrepository.com/artifact/org.scalanlp/breeze
libraryDependencies += "org.scalanlp" %% "breeze" % "0.13.2"

// https://mvnrepository.com/artifact/com.github.fommil.netlib/all
libraryDependencies += "com.github.fommil.netlib" % "all" % "1.1.2" pomOnly()

unmanagedBase := new File("/Applications/HDFView.app/Contents/MacOS")

unmanagedBase := new File("/Applications/HDFView.app/Contents/Java")



