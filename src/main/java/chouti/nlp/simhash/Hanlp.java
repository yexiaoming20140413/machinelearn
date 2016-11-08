package chouti.nlp.simhash;


import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.summary.TextRankKeyword;

import java.util.*;

/*******************************************************************************
 * Copyright (c) 2005-2016 Gozap, Inc.
 * Contributors:
 * xiaoming  on 16-10-27.
 *******************************************************************************/
public class Hanlp {
    public static void main(String args[]){

        ChoutiSegment segment = new ChoutiSegment();
        String text1="程序员(英文Programmer)是从事程序开发、维护的专业人员。一般将程序员分为程序设计人员和程序编码人员，但两者的界限并不非常清楚，特别是在中国。软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类";
        String text2="逗狗";
        List<Term> segWords1 = segment.segment(text1,null);
//        List<Term> segWords2 = segment.segment(text2,null);
        System.out.println("segwords1:"+ JSONObject.toJSONString(segWords1));
        System.out.println("textRank:"+new TextRankKeyword().getKeyword(text1));
//        System.out.println("segwords2:"+ JSONObject.toJSONString(segWords2));



//        String text3="南棒海豹…跟他爹有一拼…#军事视频# #军事新闻# #国际军事# #环球军事# #武器的秘密# #浩汉防务# #制造业强国# #航空知识# #航空科普# #军情解码# http://t.cn/RtAJo5i .";
//        String text4="【女子无证驾车被查 #扬言嫁给交警#赖一辈子[衰]】27日，交警在宁宣高速和凤收费拦截了一辆超速越野车，经查，驾车女子是无证驾驶。当女子听说要扣车并且还会有进一步处罚时，情绪非常激动，威胁交警已经给他拍照并记住警号了，如果非要处罚，她就嫁给交警，赖上一辈子。http://t.cn/Rtc4012扬子晚报";
//        String text5="鲸播报：长沙非法拆除房屋致人死亡事故7名干部被批捕】2日讯，从长沙市人民检察院获悉，长沙市岳麓区观沙岭街道茶子山村非法拆除房屋致人死亡事故的侦查与处理已有新进展。包括观沙岭街道原党工委书记、办事处主任等7名干部已于近日分别被长沙市人民检察院依法批捕。(新华社)";
//        String text6="在Mnet新舞蹈竞演节目#Hit The Stage#中，当“鬼怪”的突然出现时各位爱豆的反应。#SISTAR宝拉#、#少女时代孝渊#、#SHINee泰民#、#Infinite Hoya#、#Block B有权#、#Monsta X Shownu#、#NCT Ten#、#Twice##Momo#/#Sana#/#俞定延#/#金多贤#/#朴志效#    http://t.cn/RtcUnZ9 .";
//        String text7="【女子无证驾车被查 #扬言嫁给交警#赖一辈子[衰]】27日，交警在宁宣高速和凤收费拦截了一辆超速越野车，经查，驾车女子是无证驾驶。当女子听说要扣车并且还会有进一步处罚时，情绪非常激动，威胁交警已经给他拍照并记住警号了，如果非要处罚，她就嫁给交警，赖上一辈子。http://t.cn/Rtc4012";
//        String text8="【女子无证驾车被查 #扬言嫁给交警#】27日，交警在宁宣高速和凤收费拦截了一辆超速越野车，经查，驾车女子是无证驾驶。当女子听说要扣车并且还会有进一步处罚时，情绪非常激动，威胁交警已经给他拍照并记住警号了，如果非要处罚，她就嫁给交警，赖上一辈子。#控制不了自己情绪的女子娶不得[嘻嘻]#...全文： http://m.weibo.cn/1892327960/4002508907303169";
//        String text9="【长沙检方：涉非法强拆致居民被埋死亡案的7名干部被批捕】从长沙市人民检察院获悉，岳麓区观沙岭街道茶子山村非法拆除房屋致人死亡事故的侦查与处理已有新进展。包括观沙岭街道原党工委书记、办事处主任等7名干部已于近日分别被长沙市人民检察院依法批捕。新华网http://t.cn/RtJiHC5";


    }

}
