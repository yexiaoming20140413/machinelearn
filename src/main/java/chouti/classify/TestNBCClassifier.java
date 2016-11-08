package chouti.classify;

import chouti.nlp.simhash.ChoutiSegment;

import java.util.HashMap;
import java.util.Map;

/*******************************************************************************
 * Copyright (c) 2005-2016 Gozap, Inc.
 * Contributors:
 * xiaoming  on 16-11-1.
 *******************************************************************************/
public class TestNBCClassifier {

    public static void main(String args[]){

        String yesDocStr1 ="义勇军进行曲 \n" +
                "田汉作词,聂耳作曲 \n" +
                "起来! \n" +
                "不愿做奴隶的人们! \n" +
                "把我们的血肉, \n" +
                "筑成我们新的长城! \n" +
                "中华民族到了 \n" +
                "最危险的时候, \n" +
                "每个人被迫着 \n" +
                "发出最后的吼声! \n" +
                "起来! \n" +
                "起来! \n" +
                "起来! \n" +
                "我们万众一心, \n" +
                "冒着敌人的炮火 \n" +
                "前进, \n" +
                "冒着敌人的炮火 \n" +
                "前进! \n" +
                "前进! \n" +
                "前进!进!! \n" +
                "爱国主义精神永垂不朽!!!";

        String yesDocStr2 ="我们新中国的儿童，\n" +
                "我们新少年的先锋，\n" +
                "团结起来，\n" +
                "继承着我们的父兄，\n" +
                "不怕艰难，\n" +
                "不怕担子重，\n" +
                "为了新中国的建设而奋斗，\n" +
                "学习伟大的领袖毛泽东。\n" +
                "毛泽东，\n" +
                "新中国的太阳，\n" +
                "开辟了新中国的方向，\n" +
                "黑暗势力已从全中国扫荡，\n" +
                "红旗招展，\n" +
                "前途无限量，\n" +
                "为了新中国的建设而奋斗，\n" +
                "勇敢前进，前进，\n" +
                "跟着共产党。\n" +
                "Music\n" +
                "我们新中国的儿童，\n" +
                "我们新少年的先锋，\n" +
                "团结起来，\n" +
                "继承着我们的父兄，\n" +
                "不怕艰难，\n" +
                "不怕担子重，\n" +
                "为了新中国的建设而奋斗，\n" +
                "学习伟大的领袖毛泽东。";

        String noDocStr ="科比·布莱恩特（Kobe Bryant），1978年8月23日出生于美国宾夕法尼亚州费城，前美国职业篮球运动员，司职得分后卫/小前锋（锋卫摇摆人），整个NBA生涯（1996年-2016年）一直效力于NBA洛杉矶湖人队，是前美国职业篮球运动员乔·布莱恩特的儿子。\n" +
                "科比是NBA最好的得分手之一，突破、投篮、罚球、三分球他都驾轻就熟，几乎没有进攻盲区，单场比赛81分的个人纪录就有力地证明了这一点。除了疯狂的得分外，科比的组织能力也很出众，经常担任球队进攻的第一发起人。另外科比还是联盟中最好的防守人之一，贴身防守非常具有压迫性。\n" +
                "2016年4月14日，在结束了生涯最后一场主场对阵爵士的常规赛之后，科比·布莱恩特正式宣布退役。";


        String testDocStr ="在结束了生涯最后一场主场对阵爵士的常规赛之后";


        ChoutiSegment segment1 = new ChoutiSegment();
        ChoutiSegment segment2 = new ChoutiSegment();
        ChoutiSegment segment3 = new ChoutiSegment();

        //统计正文本特征词和词频率
        Map<String,Integer> nbcPositiveMap;
        segment1.segmentFrequency(yesDocStr1);
        nbcPositiveMap = segment1.segmentFrequency(yesDocStr2);

        //统计负文本特征和词频
        Map<String,Integer> nbcNegativeMap = segment2.segmentFrequency(noDocStr);

        //统计待分类文本特征词和词频
        Map<String,Integer> testFrequencyMap = segment3.segmentFrequency(testDocStr);

        //人工添加分词权重
        Map<String,Double> wordWeightMap = new HashMap<>();


        NBClassifier classifier = new NBClassifier(nbcPositiveMap, nbcNegativeMap, wordWeightMap);
        double[] classProb = classifier.classify(testFrequencyMap);

        classProb = classifier.normalized(classProb);
        print(classProb);

        classifier.train(testFrequencyMap, true);
        classProb = classifier.classify(testFrequencyMap);

        classProb = classifier.normalized(classProb);
        print(classProb);

    }

    private static void print(double[] classProb) {
        System.out.println("正文本 概率" + classProb[0]);
        System.out.println("负文本 概率" + classProb[1]);
        if (classProb[0] > classProb[1]) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }

    }
}
