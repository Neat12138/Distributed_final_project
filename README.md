# Distributed_final_project

**项目结构**

```
├─finalproject_BroadcastJoin	BroadcastJoin项目
│  ├─.idea
│  │  ├─artifacts
│  │  └─ZeppelinRemoteNotebooks
│  ├─Broadcast_output	保存BroadcastJoin的输出
│  ├─input	程序的输入数据
│  ├─out
│  │  └─artifacts
│  │      └─BroadcastJoin	BroadcastJoin的jar包
│  ├─src
│  │  ├─main
│  │  │  ├─java
│  │  │  │  └─finalproject_BroadcastJoin	BroadcastJoin的Spark程序
│  │  │  └─resources
│  │  └─test
│  │      └─java
│  └─target
│  │    ├─classes
│  │    │  └─finalproject_BroadcastJoin
│  │    └─generated-sources
│  │        └─annotations
│  └─generate_data.py	用于生成数据的.py文件
└─finalproject_ShuffleJoin	ShuffleJoin项目
│    ├─.idea
│    │  ├─artifacts
│    │  └─ZeppelinRemoteNotebooks
│    ├─input	程序的输入数据
│    ├─out
│    │  └─artifacts
│    │      └─ShuffleJoin	ShuffleJoin的jar包
│    ├─shuffle_output	保存ShuffleJoin的输出结果
│    ├─src
│    │  ├─main
│    │  │  ├─java
│    │  │  │  └─finalproject_ShuffleJoin	ShuffleJoin的Spark程序
│    │  │  └─resources
│    │  └─test
│    │      └─java
│    └─target
│    │    ├─classes
│    │    │  └─finalproject_ShuffleJoin
│    │    └─generated-sources
│    │        └─annotations
│    └─generate_data.py	用于生成数据的.py文件
└─README.md
```



**运行说明：**

在generate_data.py所在目录下输入以下指令生成数据：

```
python generat_data.py
```

生成数据上传到HDFS指定目录后，使用以下指令运行BroadcastJoin的jar包：

```
~/spark-2.4.7/bin/spark-submit \
--deploy-mode client \
--master yarn \
--class finalproject_BroadcastJoin.BroadcastJoin \
~/jar_path/BroadcastJoin.jar hdfs://master:9000/user/user_dir/input/emp hdfs://master:9000/user/user_dir/input/dept hdfs://master:9000/user/user_dir/BroadcastJoin_output
```

生成数据上传到HDFS指定目录后，使用以下指令运行ShuffleJoin的jar包：

```
~/spark-2.4.7/bin/spark-submit \
--deploy-mode client \
--master yarn \
--class finalproject_ShuffleJoin.ShuffleJoin \
~/jar_path/ShuffleJoin.jar hdfs://master:9000/user/user_dir/input/emp hdfs://master:9000/user/user_dir/input/dept hdfs://master:9000/user/user_dir/ShuffleJoin_output
```

