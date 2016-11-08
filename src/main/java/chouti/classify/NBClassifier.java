package chouti.classify;

import java.util.Iterator;
import java.util.Map;


/**
 * 实现描述：朴素贝叶斯分类
 *
 * @author jin.xu
 * @version v1.0.0
 *          1.先验概率P(y)= 类y下单词总数/整个训练样本的单词总数
 *          2.类条件概率P(xi|y)=(类y下单词xi在各个文档中出现过的次数之和+1)/(类y下单词总数+向量的维度)
 *          3.P(y|xi)=P(y)*P(xi|y)/P(xi)等价于P(y)*P(xi|y)
 * @see
 * @since 16-9-13 下午5:44
 */
public class NBClassifier {
    private Map<String, Double> wordWeightMap;

    //正文本文本特征词,词频
    private Map<String, Integer> nbcNegativeMap;
    //负文本文本特征词,词频
    private Map<String, Integer> nbcPositiveMap;

    private double nbcNegativePrioriProb;
    private double nbcPositivePrioriProb;

    private Long wordDifCount;
    //正文本特征词总频率
    private Long nbcNegativeWordCount;
    //负文本特征词总频率
    private Long nbcPositeiveWordCount;

    public NBClassifier(Map<String, Integer> nbcPositiveMap, Map<String, Integer> nbcNegativeMap, Map<String, Double> wordWeightMap) {
        this.wordWeightMap = wordWeightMap;

        this.wordDifCount = (long) this.wordWeightMap.size();//?????????????????

        this.nbcPositiveMap = nbcPositiveMap;
        this.nbcNegativeMap = nbcNegativeMap;

        nbcPositivePrioriProb = 1.0 * nbcPositiveMap.size() / (nbcPositiveMap.size() + nbcNegativeMap.size());
        nbcNegativePrioriProb = 1.0 * nbcNegativeMap.size() / (nbcNegativeMap.size() + nbcPositiveMap.size());

        this.nbcPositeiveWordCount = getWordCount(nbcPositiveMap);
        this.nbcNegativeWordCount = getWordCount(nbcNegativeMap);
    }

    /**
     * 增量训练
     *
     * @param docMap 　待加入训练集的数据
     * @param isbYes 　对应标签
     */
    public void train(Map<String, Integer> docMap, boolean isbYes) {
        if (isbYes) {
            this.nbcNegativeMap = mergeDocMap(this.nbcNegativeMap, docMap);
        } else {
            this.nbcPositiveMap = mergeDocMap(this.nbcPositiveMap, docMap);
        }

        nbcNegativePrioriProb = 1.0 * nbcNegativeMap.size() / (nbcNegativeMap.size() + nbcPositiveMap.size());
        nbcPositivePrioriProb = 1.0 * nbcPositiveMap.size() / (nbcNegativeMap.size() + nbcPositiveMap.size());

        this.nbcNegativeWordCount = getWordCount(nbcNegativeMap);
        this.nbcPositeiveWordCount = getWordCount(nbcPositiveMap);
    }


    /**
     * 分类文档
     *
     * @param wordFreMap 　待分类文档对应的单词－频数映射
     * @return
     */
    public double[] classify(Map<String, Integer> wordFreMap) {
        double[][] conditionalProb = new double[2][wordFreMap.size()];
        conditionalProb[0] = getProbMatrix(nbcPositiveMap, nbcPositeiveWordCount, wordFreMap);
        conditionalProb[1] = getProbMatrix(nbcNegativeMap, nbcNegativeWordCount, wordFreMap);
        double[] classProb = {nbcNegativePrioriProb, nbcPositivePrioriProb};
        for (int i = 0; i < classProb.length; ++i) {
            for (int j = 0; j < wordFreMap.size(); ++j) {
                double d1 = classProb[i];
                double d2 = conditionalProb[i][j];
                double d3 = d1 * d2;
                classProb[i] *= conditionalProb[i][j];
            }
        }
        return classProb;
    }

    /**
     * 概率归一化
     *
     * @param classProb 　原概率列表
     * @return
     */
    public double[] normalized(double[] classProb) {
        double[] classProbAfterNor = new double[classProb.length];
        double sum = 0.0;
        for (int i = 0; i < classProb.length; ++i) {
            sum += classProb[i];
        }
        for (int i = 0; i < classProb.length; ++i) {
            classProbAfterNor[i] = classProb[i] / sum;
        }
        return classProbAfterNor;
    }

    /**
     * 计算给定分类属性取值下每个特征属性不同取值的条件概率矩阵
     *
     * @param docMap         　给定分类属性取值对应的单词－频数映射
     * @param classWordCount 给定分类属性取值的单词总频数
     * @param wordFreMap     　待分类文档对应的单词－频数映射
     * @return
     *
     */
    private double[] getProbMatrix(Map<String, Integer> docMap, Long classWordCount, Map<String, Integer> wordFreMap) {
        double[] probMatrixPerClass = new double[wordFreMap.size()];
        int index = 0;
        for (Iterator iterator = wordFreMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            Long tmpCount = 0l;
            Double weight = 1.0;
            if (docMap.containsKey(key)) {
                tmpCount = (long) docMap.get(key);
            }
            if (this.wordWeightMap.containsKey(key)) {
                weight = this.wordWeightMap.get(key);
            }
            probMatrixPerClass[index++] = 1.0 * (tmpCount + 1) * weight / (classWordCount + this.wordDifCount);
        }
        return probMatrixPerClass;
    }

    /**
     * 计算docMap中所有单词频数和
     *
     * @param docMap 　单词－频数映射
     * @return
     */
    private Long getWordCount(Map<String, Integer> docMap) {
        Long totalFrequency = 0l;
        for (Iterator iterator = docMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            totalFrequency += (Integer) entry.getValue();
        }
        return totalFrequency;
    }

    /**
     * 将文档合并到训练文档集
     *
     * @param allDocMap 　训练文档集
     * @param docMap    　待增量添加文档
     * @return
     */
    private Map<String, Integer> mergeDocMap(Map<String, Integer> allDocMap, Map<String, Integer> docMap) {
        for (Iterator iterator = docMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            Integer value = (Integer) entry.getValue();
            if (allDocMap.containsKey(key)) {
                allDocMap.put(key, allDocMap.get(key) + value);
            } else {
                allDocMap.put(key, value);
            }
        }
        return allDocMap;
    }
}
