df=read.delim("part-r-00000", header=FALSE, sep="\t")
library(wordcloud)
wordcloud(df$V1,df$V2)