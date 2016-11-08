package chouti.clustering;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******************************************************************************
 * Copyright (c) 2005-2016 Gozap, Inc.
 * Contributors:
 * xiaoming  on 16-11-4.
 *******************************************************************************/
public class ClusterUtils {

    //距离公式
    public static double euclideanDistance(double[] vector1, double[] vector2) {
        int len = vector1.length;
        double sumSquared = 0.0;
        for (int i = 0; i < len; i++) {
            double v = vector1[i] - vector2[i];
            sumSquared += v * v; //平方差
        }
        return Math.sqrt(sumSquared);
    }
    //KL距离
    public static double klDistance(double[] vector1, double[] vector2){
        double result=0;
        for(int i=0;i<vector1.length;i++){
            double double_vector1=vector1[i];
            double double_vector2=vector2[i];
            result+=double_vector1*Math.log(double_vector1/double_vector2);
        }
        return result;
    }
    //（JS距离）
    public static double jsDistance(double[] vector1, double[] vector2){
        double[] q=new double[vector1.length];
        for(int j=0;j<q.length;j++){
            q[j]=(vector1[j]+vector2[j])/2;
//            if(q[j]=0)
        }
        return 0.5*(klDistance(vector1,q)+klDistance(vector2,q));
    }
    //余弦相似度
    public static double cosSim(double[] vector1, double[] vector2){
        double result = pointMulti(vector1, vector2) / sqrtMulti(vector1, vector2);
        return result;
    }
    private static double sqrtMulti(double[] vector1, double[] vector2) {
        double result = squares(vector1) * squares(vector2);
        result=Math.sqrt(result);
        return result;
    }
    // 求平方和
    private static double squares(double[] vector) {
        double res=0;
        for(double data:vector){
            res+=data*data;
        }
        return res;
    }
    //点乘
    private static double pointMulti(double[] vector1, double[] vector2) {
        if(vector1.length!=vector2.length)throw new RuntimeException();
        double result = 0;
        for(int i=0;i<vector1.length;i++){
            result+=vector1[i]*vector2[i];
        }
        return result;
    }
    public static boolean isChineseStr(String str){
        String regEx = "^[\\u4e00-\\u9fa5]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if(m.find())
            return true;
        else
            return false;
    }
    // 根据Unicode编码完美的判断中文汉字和符号
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
    public static String retainStandardAndChineseChars(String str){
        return str.replaceAll("[^\\u0000-\\u00ff\\u4e00-\\u9fa5]"," ").replaceAll("\\s+"," ");
    }
    public static String retainStandardLetterNumberAndChineseChars(String str){
        return str.replaceAll("[^0-9a-zA-Z·\\u4e00-\\u9fa5]"," ").replaceAll("\\s+"," ");
    }
    public static String retainStandardLetterAndChineseChars(String str){
        return str.replaceAll("[^a-zA-Z·\\u4e00-\\u9fa5]"," ").replaceAll("\\s+"," ");
    }
    public static String retainChineseChars(String str){
        return str.replaceAll("[^\\u4e00-\\u9fa5]"," ").replaceAll("\\s+"," ");
    }
    public final static String EMOJI_REGEX="(\\[[^\\(,\\)]+\\])";//[]
}
