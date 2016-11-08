package chouti.nlp.simhash;

import chouti.mode.TextSimHashBean;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Copyright (c) 2005-2016 Gozap, Inc.
 * Contributors:
 * xiaoming  on 16-10-28.
 *******************************************************************************/
public class TestHash {

    public static void main(String args[]){
        long startTime = System.currentTimeMillis();
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("/home/xiaoming/docs/chouti_title.dat"),"UTF-8"));
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


//            Map<Integer,String> hashMap = new HashMap<>();
            List<TextSimHashBean> simHashList = new ArrayList<>();
            for(int i =0;i < titleList.size();i++){
                String title = titleList.get(i);
                SimHash simHash1=new SimHash(title,32);
                if(StringUtils.isEmpty(simHash1.strSimHash)){
                    continue;
                }
                TextSimHashBean bean = new TextSimHashBean(title,simHash1.strSimHash);

                simHashList.add(bean);
            }
            System.out.println("开始切hash");
            SimHash simHash2 = new SimHash("123");
            for(int a = 0;a < simHashList.size();a++){
                TextSimHashBean beanA = simHashList.get(a);
                String hashA = beanA.getHash();
                for(int b = 0;b < simHashList.size();b++){
                    if(a == b){
                        continue;
                    }
                    TextSimHashBean beanB = simHashList.get(b);
                    String hashB = beanB.getHash();
                    int dist=simHash2.getDistance(hashA,hashB);
                    if( dist <= 3){
                        System.out.println("a:"+beanA.getText());
                        System.out.println("b:"+beanB.getText());

                    }
                }
            }
            System.out.println("hashMap Size:"+simHashList.size());
            System.out.println("一共耗时:"+(System.currentTimeMillis()-startTime)+"ms");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
