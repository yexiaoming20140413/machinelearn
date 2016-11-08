package chouti.clustering;

import chouti.lda.LDAPredictor;
import chouti.mode.TextSimHashBean;
import chouti.nlp.simhash.ChoutiSegment;
import chouti.nlp.simhash.SimHash;
import com.hankcs.hanlp.summary.TextRankKeyword;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*******************************************************************************
 * Copyright (c) 2005-2016 Gozap, Inc.
 * Contributors:
 * xiaoming  on 16-11-2.
 *******************************************************************************/
public class TestText2LDAModel {
    private ChoutiSegment segment = new ChoutiSegment();
    private static TextRankKeyword textRankKeyword = new TextRankKeyword();
    private static LDAPredictor predictor;
    public static void main(String args[]){
        long startTime = System.currentTimeMillis();
        predictor = new LDAPredictor("/home/xiaoming/lda/GibbsLDA++-0.2/LDA","model-final");
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("/home/xiaoming/docs/chouti_title.dat"),"UTF-8"));
            String line;
            List<String> titleList = new ArrayList<String>();
            List<Long> idList = new ArrayList<Long>();
            try {
                while ((line= br.readLine())!=null){
                    titleList.add(line.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            br.close();
            List<String> topicList = new ArrayList<>();
            for(String title:titleList){
                String[] ss = title.split(" ");
                Long id = Long.parseLong(ss[0]);
                List<String> keyWordList = textRankKeyword.getKeyword(ss[1]);
                if(!CollectionUtils.isEmpty(keyWordList)){
                    String topics="";
                    for(String word:keyWordList){
                        topics+=word;
                        topics+=" ";
                    }
                    topicList.add(topics);
                    idList.add(id);
                }
            }
            String[] segOuts = new String[topicList.size()];
            for(int i = 0;i < topicList.size();i++){
                String topic = topicList.get(i);
                segOuts[i] = topic;
            }
            double[][] vectors = predictor.getWeiboVector(Arrays.asList(segOuts));
            String weibosTagSegLdaPath= "/home/xiaoming/lda/GibbsLDA++-0.2/LDA" +File.separator+"lda.vect";
            BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(weibosTagSegLdaPath), "UTF-8"));
            for(int a =0; a < vectors.length;a++){
                String vector="";
                for(double d:vectors[a]){
                    vector+=d+" ";
                }
                vector=vector.trim();
                bw1.write(idList.get(a)+"");
                bw1.newLine();
                bw1.write(vector);//1
                bw1.newLine();
            }
            bw1.close();
            System.out.println("TestText2LDAModel use time:"+(System.currentTimeMillis()-startTime)+"-ms");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
