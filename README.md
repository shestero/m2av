### Ojective
If you want to store a large data table (>2 Gb) as a file there are almost no options except CSV or TSV. But they aren't good in representing data schema. It may case a various misformat problems with dates and other typed data.
The are a few tools to store large tables into contemporary formats like AVRO (like Sqoop), but they are tuned to write them into HDFS.
Yet a table with size of serveral Gb can be stored into the simple file system to be moved with USB stick. Especially after comression.

### m2av
This is my utility to store MySQL table (or view) into local file system. It's written in Scala and could be run on cross-platform JVM. It uses Apache Spark in standalon mode to move and write data, JDBC to read from MySQL.
Originally my output format was AVRO, but it may use others (I tried ORC).
Note there are free AVRO ODBC/JDBC drivers in the Internet (check Apache Drill MapR).

Have a nice work!
