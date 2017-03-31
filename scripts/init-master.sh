#!/bin/bash

# Start SSH server
service ssh start

# Format a new distributed filesystem as hdfs
$HADOOP_HOME/bin/hdfs namenode -format

# Start the HDFS NameNode
$HADOOP_HOME/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script hdfs start namenode

# Start YARN ResourceManager
$YARN_HOME/sbin/yarn-daemon.sh --config $HADOOP_CONF_DIR start resourcemanager

# Start the MapReduce JobHistory Server
$HADOOP_HOME/sbin/mr-jobhistory-daemon.sh --config $HADOOP_CONF_DIR start historyserver
