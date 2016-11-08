package chouti.lda;

import java.io.Serializable;


public class LDAOption implements Serializable {
	
	private static final long serialVersionUID = 526166310343530738L;
	public static final long BUFFER_SIZE_LONG = 1000000;
	public static final short BUFFER_SIZE_SHORT = 512;
	
	public static final int MODEL_STATUS_UNKNOWN = 0;
	public static final int MODEL_STATUS_EST = 1;
	public static final int MODEL_STATUS_ESTC = 2;
	public static final int MODEL_STATUS_INF = 3;
	//Specify whether we want to estimate model from scratch
	public boolean est = false;  ////是否开始训练模型
	
	//Specify whether we want to continue the last estimation
	public boolean estc = false;   ///决定是否是基于先前已有的模型基础上继续用新数据训练模型
	
	//Specify whether we want to do inference
	public boolean inf = true;    ////是否使用先前已经训练好的模型进行推断
	
	public String dir = "";  //Specify directory   ////数据结果（模型数据）保存位置
	public String dfile = "";  //Specify resource data filename    ////训练数据或原始数据文件名
	
	//Specify the model level to which you want to applied. ///
	public String modelName = "";  ///选择使用哪一个迭代的模型结果来进行推断
	
	public int K = 10;  //Specify the number of topics  ///类簇数目，谨慎设置
	
	public double alpha = 0.5;  //Specify alpha  ////平滑系数
	public double beta = 0.02;  //Specify beta

	public int niters = 1000;  //Specify the number of iterations  //迭代数目，谨慎设置
	
	//Specify the number of steps to save the model since the last save.
	//The step (counted by the number of Gibbs sampling iterations) 
	//at which the LDA model is saved to hard disk.
	public int savestep = 200;//指定把迭代结果模型保存到硬盘上的迭代跨度，即每迭代10次保存一次。
	
	//Specify the number of most likely words to be printed for each topic
	public int twords = 30;   ///对每一个类别（话题）选前多少个最大概率词项
	
	//Specify whether we include raw data in the input
	public boolean withrawdata = false;
	
	//Specify the wordmap file
	public String wordMapFileName = "wordmap.txt";  ////生成的副产品的文件名

	public static String chartSet = "utf-8";
}
