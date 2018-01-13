import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.*;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DIC 
{
	static Configuration conf;
	FileSystem fs;
	Path p;
	FSDataInputStream fsip;
	BufferedReader br;
  //Scanner scanner;
	static HashMap<String,String> hm;
	String hm_key;
	String hm_val;
  public DIC()
  {
	 try
	 {
		 conf = new Configuration();
		 fs=FileSystem.get(conf);
		 p=new Path("new_lemmatizer.csv");
		 InputStreamReader isr=new InputStreamReader(fs.open(p));
		 br=new BufferedReader(isr);
     //scanner=new Scanner(isr);
		 hm=new HashMap<String,String>();
		 String x;
		 //scanner.useDelimiter("\n");
			while((x=br.readLine())!=null)
			{
        if(x==null)
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
				/*String line=br.readLine();
				StringTokenizer st=new StringTokenizer(line,",");
				int c=st.countTokens();
				if(c!=3)
				{
					
				}
				else
				{
					hm_key=st.nextToken();
					st.nextToken();
					hm_val=st.nextToken();
					hm.put(hm_key, hm_val);
				}*/
			}		
	 }
	 catch(Exception e)
	 {
		 e.printStackTrace();
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




  public static class TokenizerMapper extends Mapper<Object, Text, Text, MyMapWritable>
  {

    private Text word = new Text();
    Text loc =new Text();
    IntWritable index=new IntWritable();
    //Text doc=new Text();
    //Text chli=new Text();
    MyMapWritable mw;
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException
    {
      String x=value.toString();
      if(x.equals(""))
      {

      }
      else
      {
          StringTokenizer st=new StringTokenizer(x,"\t");
          String location=st.nextToken();
          location=location.replaceAll("<","");
          location=location.replaceAll(">","");
          loc.set(location);
          //String docid;
          //String chline;
          //StringTokenizer st2=new StringTokenizer(location);
          //docid=st2.nextToken()+st2.nextToken();
          //chline=st2.nextToken();
          String line=st.nextToken();
          line=line.replaceAll("\\p{P}","");
          line=line.replaceAll("-","");
          line=line.toLowerCase();
          String t;
          String lemma;
          //doc.set(docid);
          //chli.set(chline);
          StringTokenizer st1=new StringTokenizer(line);
          int i=0;
          while(st1.hasMoreTokens())
          {
            i++;
            index.set(i);
            mw=new MyMapWritable();
            t=st1.nextToken();
            t=t.replaceAll("j","i");
            t=t.replaceAll("v","u");
            if(hm.containsKey(t))
            {
              lemma=hm.get(t);
              StringTokenizer st3=new StringTokenizer(lemma,",");
              while(st3.hasMoreTokens())
              {
                  word.set(st3.nextToken());
                  //mw.put(doc,chli);
                  mw.put(loc,index);
                  context.write(word,mw);    
              }
              
            }
            else
            {
              word.set(t);  
              //mw.put(doc,chli);
              mw.put(loc,index);
              context.write(word,mw);
            }
            
          } 
      }
      
    }
  }

  public static class IntSumReducer extends Reducer<Text,MyMapWritable,Text,MyMapWritable> 
  {
    // IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<MyMapWritable> values, Context context) throws IOException, InterruptedException 
    {
      MyMapWritable temp=new MyMapWritable();
      for (MyMapWritable val : values) 
      {
        Set s=val.keySet();
        for(Object k : s)
        {
            Text loc=(Text)k;
            IntWritable index=(IntWritable)val.get(k);
            /*if(temp.containsKey(k))
            {
              Text docid=(Text)k;
              String x=temp.get(k).toString();
              String y=val.get(k).toString();
              String z=x+","+y;
              Text chline=new Text();
              chline.set(z); 
              temp.put(docid,chline);
            }
            else
            {
              Text chline=(Text)val.get(k);
              Text docid=(Text)k;
              temp.put(docid,chline);  
            }
*/           temp.put(loc,index); 
            //context.write(key,temp);
        }     
      }

      context.write(key,temp);

      
    }
  }

  public static void main(String[] args) throws Exception 
  {
	DIC d=new DIC();  
    Job job = Job.getInstance(conf, "DIC");
    job.setJarByClass(DIC.class);
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
