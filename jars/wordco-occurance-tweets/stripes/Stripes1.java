import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.util.*;
public class Stripes1 {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, MyMapWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    private Text word1 = new Text();
    MyMapWritable mw;
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      String token[];
      token=new String[itr.countTokens()];
      int m=0;
      while (itr.hasMoreTokens()) 
      {
        token[m]=itr.nextToken();
        m++;
      }
      String res;
      for(int i=0;i<token.length;i++)
      {
        word1.set(token[i]);
        mw=new MyMapWritable();
        for(int j=0;j<token.length;j++)
        {
          if(i==j)
          {
          }
          else
          {
            res=token[i]+" "+token[j];
            word.set(res);
            mw.put(word,one);
            context.write(word1,mw);
          }
            
        }   
      }
    }
  }
  public static class MyMapWritable extends MapWritable
  {
    public String toString()
    {
      StringBuilder result = new StringBuilder();
        Set keySet = this.keySet();

        for (Object key : keySet) {
            result.append("{" + key.toString() + " = " + this.get(key) + "}");
        }
        return result.toString();
    }
  }
  public static class IntSumReducer
       extends Reducer<Text,MyMapWritable,Text,MyMapWritable> {
    //Text result=new Text();
    public void reduce(Text key, Iterable<MyMapWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
    
      MyMapWritable temp=new MyMapWritable();
      for (MyMapWritable val : values) 
      {
        Set s=val.keySet();
        for(Object k : s)
        {
          if(temp.containsKey(k))
          {
            IntWritable a=(IntWritable)temp.get(k);
            int b=a.get();
            b++;
            IntWritable c=new IntWritable(b);
            Text kk=(Text)k;
            temp.put(kk,c); 
          }
          else
          {
            IntWritable iw=(IntWritable)val.get(k);
            Text kk=(Text)k;
            temp.put(kk,iw);
          }
          
        }        
      }
      context.write(key,temp);
      }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Stripes1");
    job.setJarByClass(Stripes1.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(MyMapWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}