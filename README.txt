
							       ****************************README**********************************




1) WORD COUNT ON TWEETS:

   In order to collect tweets and to filter hashtags from them, I have used the following jupyter notebook 
   hashtags.ipynb - from LAB4/ancillary-code
   The output of this notebook is a text file - test.txt

   PART A - Getting the word count of hashtags

   Before executing the following commands, you should have the following files into the current directory from where you will execute the commands:
   test.txt - from LAB4/input/wordcount-tweets/input-MR 	
   wcl.jar - from LAB4/jars/wordcount-tweets

1) Create directory test in hdfs by using following command:
	hdfs dfs -mkdir -p ~/test

2) Put the input file into hdfs by using the following command: 
   	hdfs dfs -put test.txt ~/test/  

3) Execute the MR tasks by using the following command:
	hadoop jar wcl.jar WordCloud ~/test/ ~/output-wcl

4) Get the output file from hdfs by using the following command:
	hdfs dfs -get ~/output-wcl

5) The output will be of following format:

	#Barcelon	12

	#Barcelon has occured 12 times in the document	

SAMPLE OUTPUT FILE - LAB4/output/wordcount-tweets/output-MR

------------------------------------------------------------------------------------------------------------------------------------------------------

   PART B - Creating the word cloud

   In order to create the word cloud, I have used R-Studio as I had issues in installing the package wordcloud in jupyter notebook.
   I have submitted the word-cloud.R file and the wordcloud exported as a pdf.

   You should have the following files into the current directory:

   part-r-00000 - from LAB4/input/wordcount-tweets/input-wordcloud	 
   word-cloud.R - from LAB4/ancillary-code

   Open the file word-cloud.R with RStudio and run.
   
SAMPLE OUTPUT FILE - LAB4/output/wordcount-tweets
   




=======================================================================================================================================================






2) WORD CO-OCCURANCE ON TWEETS

   In order to collect and clean tweets, I have used the following jupyter notebook 
   tweets.ipynb - from LAB4/ancillary-code
   The output of this notebook is a text file - data.txt
   
   PART - A (PAIRS)	
   Before executing the following commands, you should have the following files into the current directory from where you will execute the commands:
   data.txt - from LAB4/input/wordco-occurance-tweets 	
   wc.jar - from LAB4/jars/wordco-occurance-tweets/pairs

1) Create directory test in hdfs by using following command:
	hdfs dfs -mkdir -p ~/test

2) Put the input file into hdfs by using the following command: 
   	hdfs dfs -put data.txt ~/test/  

3) Execute the MR tasks by using the following command:
	hadoop jar wc.jar WordCount ~/test/ ~/output-wcp

4) Get the output file from hdfs by using the following command:
	hdfs dfs -get ~/output-wcp

5) The output will be of following format:

	A Argentina	8

	A and Argentina is a pair and 8 is the number of times it has co-occured	

SAMPLE OUTPUT FILE - LAB4/output/wordco-occurance-tweets/output-pairs
---------------------------------------------------------------------------------------------------------------------------------------------------------

   PART - B (STRIPES)
   Before executing the following commands, you should have the following files into the current directory from where you will execute the commands:
   data.txt - from LAB4/input/wordco-occurance-tweets 	
   sp.jar - from LAB4/jars/wordco-occurance-tweets/stripes

1) Create directory test in hdfs by using following command:
	hdfs dfs -mkdir -p ~/test

2) Put the input file into hdfs by using the following command: 
   	hdfs dfs -put data.txt ~/test/  

3) Execute the MR tasks by using the following command:
	hadoop jar sp.jar Stripes1 ~/test/ ~/output-wcs

4) Get the output file from hdfs by using the following command:
	hdfs dfs -get ~/output-wcs

5) The output will be of following format:

	AKBakb	{AKBakb soccer = 1}{AKBakb football = 2}{AKBakb akbsoccer = 1}

	AKBakb is the word that has co-occured with soccer once, football twice and akbsoccer once. 

SAMPLE OUTPUT FILE - LAB4/output/wordco-occurance-tweets/output-stripes






==========================================================================================================================================================






3) ACTIVITY 1 - WORD COUNT ON CLASSICAL LATIN TEXT

   Before executing the following commands, you should have the following files into the current directory from where you will execute the commands:
   lucan.bellum_civile.part.1.tess - from LAB4/input/Activity-1
   vergil.aeneid.tess - from LAB4/input/Activity-1
   new_lemmatizer.csv - from LAB4/input/Activity-1
   dic.jar - from LAB4/jars/Activity-1

1) Create directory test in hdfs by using following command:
	hdfs dfs -mkdir -p ~/test

2) Put the input files into hdfs by using the following commands: 
   	hdfs dfs -put lucan.bellum_civile.part.1.tess ~/test/  
	hdfs dfs -put vergil.aeneid.tess ~/test/  

3) Put the file new_lemmatizer.csv into hdfs by using the following command:
   	hdfs dfs -put new_lemmatizer.csv /user/hadoop/

4) Execute the MR tasks by using the following command:
	hadoop jar dic.jar DIC ~/test/ ~/output-ac1

5) Get the output file from hdfs by using the following command:
	hdfs dfs -get ~/output-ac1

6) The output will be of following format:

	abas	{verg. aen. 10.427 = 7}{verg. aen. 10.170 = 3}{verg. aen. 1.121 = 4}{verg. aen. 3.286 = 6}
	
	abas is the lemma, which has occured in the given 4 locations.

	Consider the first location {verg. aen. 10.427 = 7}:
	verg. aen. is the doc id
	10 is the chapter number
	427 is the line number 
	7 is the index of the word in the line in the input file, starting from 0.

SAMPLE OUTPUT FILE - LAB4/output/Activity-1/output-lemma





===========================================================================================================================================================






4) ACTIVITY 2 - WORD CO-OCCURANCE ON MULTIPLE DOCUMENTS

1) ACTIVITY 2 - PART A (2 grams)

   Before executing the following commands, you should have the following files into the current directory from where you will execute the commands:
   lucan.bellum_civile.part.1.tess - from LAB4/input/Activity-2
   vergil.aeneid.tess - from LAB4/input/Activity-2
   new_lemmatizer.csv - from LAB4/input/Activity-2
   p41.jar - from LAB4/jars/Activity-2/Activity2-A

1) Create directory test in hdfs by using following command:
	hdfs dfs -mkdir -p ~/test

2) Put the input files into hdfs by using the following commands: 
   	hdfs dfs -put lucan.bellum_civile.part.1.tess ~/test/  
	hdfs dfs -put vergil.aeneid.tess ~/test/  

3) Put the file new_lemmatizer.csv into hdfs by using the following command:
   	hdfs dfs -put new_lemmatizer.csv /user/hadoop/

4) Execute the MR tasks by using the following command:
	hadoop jar p41.jar Part41 ~/test/ ~/output-ac2a

5) Get the output file from hdfs by using the following command:
	hdfs dfs -get ~/output-ac2a

6) The output will be of following format:

	a ita	<<boe. quo. pr.20-4 19,4><boe. quo. 5.25-9 32,13>>

	a and ita is a pair of lemmas, which has co-occured in the given 2 locations.

	Consider the first location <boe. quo. pr.20-4 19,4>:
	boe. quo. pr. is the doc id
	20 is the chapter number
	4 is the line number 
	19 and 4 are the index of the words in the line in the input file, starting from 0.


SAMPLE OUTPUT FILE - LAB4/output/Activity-2/output-2a
PLOT - 	LAB4/ancillary-code/plot1.pdf
	The plot of time vs number of documents is created using the following jupyter notebook
	LAB4/ancillary-code/plots.ipynb

--------------------------------------------------------------------------------------------------------------------------------------------------------------

2) ACTIVITY 2 - PART B (3 grams)

   Before executing the following commands, you should have the following files into the current directory from where you will execute the commands:
   lucan.bellum_civile.part.1.tess - from LAB4/input/Activity-2
   vergil.aeneid.tess - from LAB4/input/Activity-2
   new_lemmatizer.csv - from LAB4/input/Activity-2
   ng.jar - from LAB4/jars/Activity-2/Activity2-B

1) Create directory test in hdfs by using following command:
	hdfs dfs -mkdir -p ~/test

2) Put the input files into hdfs by using the following commands: 
   	hdfs dfs -put lucan.bellum_civile.part.1.tess ~/test/  
	hdfs dfs -put vergil.aeneid.tess ~/test/  

3) Put the file new_lemmatizer.csv into hdfs by using the following command:
   	hdfs dfs -put new_lemmatizer.csv /user/hadoop/

4) Execute the MR tasks by using the following command:
	hadoop jar ng.jar Ngrams ~/test/ ~/output-ac2b

5) Get the output file from hdfs by using the following command:
	hdfs dfs -get ~/output-ac2b

6) The output will be of following format:

	ab iuppiter superus	<<verg. aen. 1.380 5,6,7><verg. aen. 6.123 6,7,8>>

	ab, iuppiter and superus is a trigram of lemmas, which has co-occured in the given 2 locations.

	Consider the first location <verg. aen. 1.380 5,6,7>:
	verg. aen. is the doc id
	1 is the chapter number
	380 is the line number 
	5,6,7 are the index of the original words in the line in the input file, starting from 0.


SAMPLE OUTPUT FILE - LAB4/output/Activity-2/output-2b
PLOT -  LAB4/ancillary-code/plot2.pdf
	The plot of time vs number of documents is created using the following jupyter notebook
	LAB4/ancillary-code/plots.ipynb




===============================================================================================================================================================


