package chouti.nlp.simhash;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******************************************************************************
 * Copyright (c) 2005-2016 Gozap, Inc.
 * Contributors:
 * xiaoming  on 16-10-27.
 *******************************************************************************/
public class ChoutiSegment {



//    private Segment segment = HanLP.newSegment().enableCustomDictionary(true).enableAllNamedEntityRecognize(false).enableNameRecognize(true).enableTranslatedNameRecognize(true)
//            .enableNumberQuantifierRecognize(true);

    private Segment segment = HanLP.newSegment().enableCustomDictionary(true).enableOrganizationRecognize(true).enablePlaceRecognize(true).enableTranslatedNameRecognize(true).enableJapaneseNameRecognize(true).enablePartOfSpeechTagging(true);

//    private Segment crfSegment = new CRFSegment().enableCustomDictionary(true).enableAllNamedEntityRecognize(true);

    private Segment nshortSegment =new NShortSegment().enableCustomDictionary(true).enableAllNamedEntityRecognize(true);


    private Map<String,Integer> frequencyMap = new HashMap<>();

    private static int MIN_SEG_WORD_LEN=2;



    public ChoutiSegment(){

    }
    public static Set<String> holdPosSet=new HashSet<String>();
    static {
        holdPosSet.add("n");      //名词
        holdPosSet.add("nr");     //人名
        holdPosSet.add("ns");     //地名
        holdPosSet.add("nt");     //机构团体名
        holdPosSet.add("nz");     //其他专名
        holdPosSet.add("vd");     //副动词
        holdPosSet.add("vn");     //动名词
        holdPosSet.add("an");     //名形词
        holdPosSet.add("nis");    //机构后缀
        holdPosSet.add("nrf");    //nrf 音译人名
        holdPosSet.add("v");      //动词
        holdPosSet.add("g");      //学术词汇
        holdPosSet.add("nnt");      //职务职称
    }

    public List<Term> segment(String text,String[] retainPos){
        Set<String> retainPosSet=new HashSet<String>();
        if(retainPos!=null){
            for(String pos:retainPos)
                retainPosSet.add(pos);
        }
        text=text.replaceAll("@\\S+ ","");
        text=text.replaceAll("@[0-9a-zA-Z_\\-\\u4e00-\\u9fa5]{2,30}"," ");//去@某某
        text=text.replaceAll("((http|ftp|https):\\/\\/){0,1}[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?","");//去网址
        text=text.replaceAll("]","");
        Pattern p= Pattern.compile("#([^#]+)#");
        Matcher m=p.matcher(text);
        Set<String> topics=new HashSet<String>();
        StringBuffer sb=new StringBuffer();
        while (m.find()){
            for(int i=1;i<=m.groupCount();i++){
                String str=m.group(i);
                topics.add(str);
                m.appendReplacement(sb,"");
            }
        }
        m.appendTail(sb);

        List<Term> terms= segment.seg(sb.toString());
        List<Term> resultTerm = new ArrayList<>();
        CoreStopWordDictionary.apply(terms);
        if(CollectionUtils.isEmpty(terms) || terms.size() < 2){
            return null;
        }
        for(Term term:terms){
            String pos=term.nature.name();
            String word=term.word.toString();
            if(!holdPosSet.contains(pos)){
                continue;
            }
            if(word.length()==1)continue;
            resultTerm.add(term);
        }
        return resultTerm;

    }

    /**
     * 分词统计词频
     * @return
     */
    public Map<String,Integer> segmentFrequency(String text){
        Set<String> retainPosSet=new HashSet<String>();
        text=text.replaceAll("@\\S+ ","");
        text=text.replaceAll("@[0-9a-zA-Z_\\-\\u4e00-\\u9fa5]{2,30}"," ");//去@某某
        text=text.replaceAll("((http|ftp|https):\\/\\/){0,1}[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?","");//去网址
        text=text.replaceAll("]","");
        Pattern p= Pattern.compile("#([^#]+)#");
        Matcher m=p.matcher(text);
        Set<String> topics=new HashSet<String>();
        StringBuffer sb=new StringBuffer();
        while (m.find()){
            for(int i=1;i<=m.groupCount();i++){
                String str=m.group(i);
                topics.add(str);
                m.appendReplacement(sb,"");
            }
        }
        m.appendTail(sb);

        List<Term> terms= segment.seg(sb.toString());
        CoreStopWordDictionary.apply(terms);

        for(Term term:terms){
            String pos=term.nature.name();
            String word=term.word.toString();
            if(StringUtils.isEmpty(word)){
                continue;
            }
            if(!holdPosSet.contains(pos)){
                continue;
            }
            if(word.length()==1)continue;
            if(frequencyMap.containsKey(word)){
                frequencyMap.put(word,frequencyMap.get(word)+1);
            }else{
                frequencyMap.put(word,1);
            }
        }
        return frequencyMap;
    }


}
