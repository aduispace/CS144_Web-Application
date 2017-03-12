val fileRdd = sc.textFile("twitter.edges")
// RDD[ String ]

val splitRdd = fileRdd.map( line => line.split(": ") )
// RDD[ Array[ String ]

val yourRdd = splitRdd.flatMap( arr => {
  val follwering = arr( 1 )
  val words = follwering.split( "," )
  words.map( word => ( word, 1 ) )
} )
val wordCounts = yourRdd.reduceByKey((a,b) => a+b)
val result = wordCounts.filter(_._2 > 1000)
val finalR = result.sortBy(_._2, false)
finalR.saveAsTextFile("output")
System.exit(0)