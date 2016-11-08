package chouti.clustering;

import chouti.lda.LDAPredictor;
import chouti.nlp.simhash.ChoutiSegment;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*******************************************************************************
 * Copyright (c) 2005-2016 Gozap, Inc.
 * Contributors:
 * xiaoming  on 16-11-3.
 *******************************************************************************/
public class TestCreateLDAModel {
    private static ChoutiSegment segment = new ChoutiSegment();
    public static void main(String args[]) throws Exception{
        long startTime = System.currentTimeMillis();
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("/home/xiaoming/docs/chouti_title.dat"),"UTF-8"));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/home/xiaoming/lda/GibbsLDA++-0.2/LDA/Test.dat"),"UTF-8"));
            String line;
            List<String> titleList = new ArrayList<String>();
            try {
                while ((line= br.readLine())!=null){
                    titleList.add(line.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            br.close();
            List<String> ldaTextList = new ArrayList<>();
            for(int i =0;i < titleList.size();i++){
                String title = titleList.get(i);
                List<Term> terms = segment.segment(title,null);
                if(null == terms || terms.size() <= 0){
                    continue;
                }
                String text ="";
                for(int a = 0;a < terms.size();a++){
                    Term term = terms.get(a);
                    text += term.word.trim();
                    text +=" ";
                }
                ldaTextList.add(text);
            }
            bw.write(ldaTextList.size()+"");
            for(String text:ldaTextList){
                bw.newLine();
                bw.write(text);
            }
            bw.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
