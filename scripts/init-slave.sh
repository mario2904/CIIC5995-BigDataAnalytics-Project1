#!/bin/bash

# Start SSH server
service ssh start

# Start HDFS DataNode
$HADOOP_HOME/sbin/hadoop-daemons.sh --config $HADOOP_CONF_DIR --script hdfs start datanode

# Run a script to start a NodeManager
$YARN_HOME/sbin/yarn-daemons.sh --config $HADOOP_CONF_DIR start nodemanager
