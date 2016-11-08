package chouti.lda;

import org.apache.log4j.Logger;


import java.util.*;

public class LDAPredictor {

	private Logger logger= Logger.getLogger(LDAPredictor.class);
	private Inferencer inferencer;

	//输入模型文件地址初始化
	public LDAPredictor(String dir, String modelName) {
		logger.info("LDAPredictor start");
		LDAOption option = new LDAOption();
		
		option.dir = dir;
		option.modelName = modelName;
		option.inf = true;
		inferencer = new Inferencer();
		inferencer.init(option);
		logger.info("LDAPredictor ok");
	}
	public boolean isExistWords(String[] words){
		boolean flag;
		for(String word:words){
			flag=inferencer.globalDict.word2id.containsKey(word);
			if(flag)return true;
		}
		return false;
	}
	public boolean isExistWords(String wordStr){
		wordStr=wordStr.replaceAll("　"," ");
		String[] words = wordStr.split("[ \\t\\n]");
		return isExistWords(words);
	}

	public Model inference(String[] data){
		return inferencer.inference(data);
	}

	public double[][] weibosVector(List<String> weiboBatSeg){
		double[][] weiboLabels = getWeiboVector(weiboBatSeg);
		return weiboLabels;
	}

	public double[][] getWeiboVector(List<String> weibosSeg){
		String[] strsWeiboSeg=new String[weibosSeg.size()];
		Model model = inference(weibosSeg.toArray(strsWeiboSeg));
//		System.out.println("theta row="+model.theta.length+",col="+model.theta[0].length);
//		System.out.println("phi row="+model.phi.length+",col="+model.phi[0].length);
//		System.out.println("V="+inferencer.trnModel.V+",K="+inferencer.trnModel.K);
		int size=weibosSeg.size();
		double[][] result=new double[size][];
		for(int i=0;i<size;i++){
			double [] dist = model.theta[i];
			result[i]=dist;
		}
		return result;
	}
	public String[] getWeiboLabels(String[] weibosSeg){//weibosSeg 分词好了的结果
		String[] result=new String[weibosSeg.length];
		for(int i=0;i<weibosSeg.length;i++){
			Model model = inference(new String[]{weibosSeg[i]});
			result[i]=getWeiboLabel(model,0);//第i篇微博
		}
		return result;
	}
	private String getWeiboLabel(Model model, int weiboIndex){
		double [] dist = model.theta[weiboIndex];
		int topicMaxIndex=0;
		double topicMaxPro=dist[0];
		for(int i=1;i<dist.length;i++){
			if(dist[i]>topicMaxPro){
				topicMaxIndex=i;
				topicMaxPro=dist[i];
			}
		}
		String weiboLabel=getKeysFromTopicI(topicMaxIndex, model,8);
		return weiboLabel;
	}
	private String getKeysFromTopicI(int iTopic,Model model,int topKwords){
		double terms_pro[]=model.phi[iTopic];//phi:行是topic编号，列是lid2gid的key(_id)

		double[] terms_pro2=terms_pro.clone();
		int[] index=sortArr(terms_pro2);
		String result="";
		Set<String> tags=new HashSet<String>();
		for(int j=0;j<index.length&&j<topKwords;j++){
			int _id=j;
			String word=model.data.localDict.id2word.get(index[_id]);
			boolean isOk=true;
			for(String tag:tags){
				if(tag.contains(word))isOk=false;
			}
			if(isOk){
				tags.add(word);
				result+=word+" ";
			}
		}
		return result;
	}
	private static int[] sortArr(double [] ary){
		//java排序一个数组（数组元素有重复的）,并且记住新数组的元素在原数组中的位置
		int[] index = new int[ary.length];
		for (int i = 0; i < index.length; i++) {
			index[i] = i;
		}
		for (int i = 0; i < ary.length-1; i++) {
			for (int j = i+1; j < ary.length; j++) {
				if(ary[i]<ary[j]){
					double temp = ary[i];
					ary[i]=ary[j];
					ary[j]=temp;
					int p=index[i];
					index[i]=index[j];
					index[j]=p;
				}
			}
		}
		return index;
	}
}
