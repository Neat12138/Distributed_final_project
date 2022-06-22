package finalproject_ShuffleJoin;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;

public class ShuffleJoin {
    public static void run(String[] args) {
        /* 步骤1：通过SparkConf设置配置信息，并创建SparkContext */
        SparkConf conf = new SparkConf();
        conf.setAppName("ShuffleJoinJava");
//        conf.setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

//        long start_time = System.currentTimeMillis();

        JavaPairRDD<String, String> departmentRDD = sc.textFile(args[1]).mapToPair(
                new PairFunction<String, String, String>() {
                    @Override
                    public Tuple2<String, String> call(String s) throws Exception {
                        String[] line = s.split("\t");
                        return new Tuple2<String, String>(line[0], line[1]);
                    }
                }
        );


        JavaPairRDD<String, String> employeeRDD = sc.textFile(args[0]).mapToPair(
                new PairFunction<String, String, String>() {
                    @Override
                    public Tuple2<String, String> call(String s) throws Exception {
                        String[] line = s.split("\t");
                        return new Tuple2<String, String>(line[2], line[0] + "\t" + line[1]);
                    }
                }
        );

        JavaPairRDD<String, Tuple2<Iterable<String>, Iterable<String>>> result = departmentRDD.cogroup(employeeRDD);
//        result.foreach(
//                new VoidFunction<Tuple2<String, Tuple2<Iterable<String>, Iterable<String>>>>() {
//                    @Override
//                    public void call(Tuple2<String, Tuple2<Iterable<String>, Iterable<String>>> stringTuple2Tuple2) throws Exception {
//                        System.out.println(stringTuple2Tuple2._2._2 + "\n");
//                    }
//                }
//        );
        result.flatMapToPair(
                new PairFlatMapFunction<Tuple2<String, Tuple2<Iterable<String>, Iterable<String>>>, String, String>() {
                    @Override
                    public Iterator<Tuple2<String, String>> call(Tuple2<String, Tuple2<Iterable<String>, Iterable<String>>> stringTuple2Tuple2) throws Exception {
                        ArrayList<Tuple2<String, String>> res = new ArrayList<>();
                        for (String s1 : stringTuple2Tuple2._2._1) {
                            for (String s2 : stringTuple2Tuple2._2._2) {
                                res.add(new Tuple2<String, String>(s1, s2+ "\t" + stringTuple2Tuple2._1));
                            }
                        }
                        return res.iterator();
                    }
                }
        ).saveAsTextFile(args[2]);

        sc.close();

    }

    public static void main(String[] args) {
        run(args);
    }
}
