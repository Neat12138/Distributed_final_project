package finalproject_BroadcastJoin;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class BroadcastJoin {
    public static void run(String[] args) {
        /* 步骤1：通过SparkConf设置配置信息，并创建SparkContext */
        SparkConf conf = new SparkConf();
        conf.setAppName("BroadcastJoinJava");
//        conf.setMaster("local"); // 仅用于本地进行调试，如在集群中运行则删除本行
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<String, String> departmentRDD = sc.textFile(args[1]).mapToPair(
                new PairFunction<String, String, String>() {
                    @Override
                    public Tuple2<String, String> call(String s) throws Exception {
                        String[] line = s.split("\t");
                        return new Tuple2<String, String>(line[0], line[1]);
                    }
                }
        );
        Map<String, String> temp = departmentRDD.collectAsMap();
        Broadcast<Map<String, String>> department = sc.broadcast(temp);

        JavaPairRDD<String, String> employeeRDD = sc.textFile(args[0]).mapToPair(
                new PairFunction<String, String, String>() {
                    @Override
                    public Tuple2<String, String> call(String s) throws Exception {
                        String[] line = s.split("\t");
                        // (dept, name + empID)
                        return new Tuple2<String, String>(line[2], line[0] + "\t" + line[1]);
                    }
                }
        );
//        employeeRDD.foreach(
//                new VoidFunction<Tuple2<String, String>>() {
//                    @Override
//                    public void call(Tuple2<String, String> stringStringTuple2) throws Exception {
//                        Map<String, String> departmentBroadCastValue = department.getValue();
//                        System.out.println(departmentBroadCastValue);
//                    }
//                }
//        );
        employeeRDD.map(
                new Function<Tuple2<String, String>, Object>() {
                    @Override
                    public Object call(Tuple2<String, String> v1) throws Exception {
                        Map<String, String> departmentBroadCastValue = department.getValue();
                        if (departmentBroadCastValue.containsKey(v1._1)){
                            String left = departmentBroadCastValue.get(v1._1);
                            return new Tuple2<String, String>(v1._2 + "\t", v1._1 + "\t" +  left);
                        }
                        else {
                            return null;
                        }
                    }

                }
        ).saveAsTextFile(args[2]);

        sc.close();
    }
    public static void main(String[] args) {
        run(args);
    }
}