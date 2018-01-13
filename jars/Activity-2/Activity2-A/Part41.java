import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.*;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Part41 
{
	static Configuration conf;
	FileSystem fs;
	Path p;
	FSDataInputStream fsip;
	BufferedReader br;
  static HashMap<String,String> hm;
	String hm_key;
	String hm_val;
  public Part41()
  {
	 try
	 {
		 conf = new Configuration();
		 fs=FileSystem.get(conf);
		 p=new Path("new_lemmatizer.csv");
		 InputStreamReader isr=new InputStreamReader(fs.open(p));
		 br=new BufferedReader(isr);
		 hm=new HashMap<String,String>();
		 String x;
			while((x=br.readLine())!=null)
			{
        if(x.equals(""))
        {

        }
        else
        {
            StringTokenizer st=new StringTokenizer(x,",");
            hm_key=st.nextToken();
            while(st.hasMoreTokens())
            {
              
              if(hm.containsKey(hm_key))
              {
                hm_val=st.nextToken();
                String temp=hm.get(hm_key)+","+hm_val;
                hm.put(hm_key, temp);
                
              }
              else
              {
                hm_val=st.nextToken();
                hm.put(hm_key, hm_val);
              }
            }
        }  
	
			}		
	 }
	 catch(Exception e)
	 {
		 e.printStackTrace();
	 }
	 
	 
  }

  public static class TokenizerMapper extends Mapper<Object, Text, Text, Text>
  {

    private Text word = new Text();
    Text loc =new Text();
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException
    {
      String tokens[];
      String x1=value.toString();
      if(x1.equals(""))
      {

      }
      else
      {
          StringTokenizer st=new StringTokenizer(x1,">");
          String location=st.nextToken();
          location=location.replaceAll("<","");
          location=location.replaceAll(">","");
          location=location.trim();
          String line=st.nextToken();
          line=line.replaceAll("\\p{P}","");
          line=line.replaceAll("\\d","");
          line=line.replaceAll("-","");
          line=line.toLowerCase();
          String t;
          StringTokenizer st1=new StringTokenizer(line);
          int m=0;
          tokens=new String[st1.countTokens()];
          while(st1.hasMoreTokens())
          {
            t=st1.nextToken();
            t=t.replaceAll("j","i");
            t=t.replaceAll("v","u");
            tokens[m]=t;
            m++;
          }

          String res;
          for(int i=0;i<tokens.length;i++)
          {
            //word1.set(tokens[i]);
            for(int j=0;j<tokens.length;j++)
            {
              String x=tokens[i];
              String y=tokens[j];
              String l1,l2;
              if(i==j)
              {
              }
              else
              {
                if((hm.containsKey(x))&&(hm.containsKey(y)))
                {
                  l1=hm.get(tokens[i]);
                  l2=hm.get(tokens[j]);

                  StringTokenizer st2=new StringTokenizer(l1,",");
                  StringTokenizer st3=new StringTokenizer(l2,",");
                  for(int p=0;p<st2.countTokens();p++)
                  {
                    String lemma1=st2.nextToken();
                    for(int q=0;q<st3.countTokens();q++)
                    {
                      res=lemma1+" "+st3.nextToken();
                      word.set(res);
                      loc.set(location+" "+i+","+j);
                      context.write(word,loc);  
                    }
                       
                  }

                }
                else if(hm.containsKey(x))
                {
                  l1=hm.get(tokens[i]);
                  StringTokenizer st2=new StringTokenizer(l1,",");
                  
                  for(int p=0;p<st2.countTokens();p++)
                  {
                      res=st2.nextToken()+" "+y;  
                      word.set(res);
                      loc.set(location+" "+i+","+j);
                      context.write(word,loc);  
                  }

                }
                else if(hm.containsKey(y))
                {
                  l2=hm.get(tokens[j]);
                  StringTokenizer st2=new StringTokenizer(l2,",");
                  for(int q=0;q<st2.countTokens();q++)
                  {
                      res=x+" "+st2.nextToken();  
                      word.set(res);
                      loc.set(location+" "+i+","+j);
                      context.write(word,loc); 
                  }
                }
                else
                {
                  res=x+" "+y;
                  word.set(res);
                  loc.set(location+" "+i+","+j);
                  context.write(word,loc);
                }
            
              }
                
            }   
          }
            
          } 
      }
      
  }

  public static class IntSumReducer extends Reducer<Text,Text,Text,Text> 
  {
 
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException 
    {
      Text result=new Text();
      StringBuilder res = new StringBuilder();
      String temp;
      for (Text val : values) 
      {
        temp=val.toString();
        if(temp.equals(""))
        {

        }
        else
        {
            res.append("<"+temp+">");
        }
        
      }
      result.set(res.toString());
      context.write(key,result);
    }
  }

  public static void main(String[] args) throws Exception 
  {
	Part41 d=new Part41();  
    Job job = Job.getInstance(conf, "Part41");
    job.setJarByClass(Part41.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}

