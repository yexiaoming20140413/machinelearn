package chouti.clustering;

import com.hankcs.hanlp.seg.common.Term;

import java.io.*;
import java.util.*;

/*******************************************************************************
 * Copyright (c) 2005-2016 Gozap, Inc.
 * Contributors:
 * xiaoming  on 16-11-3.
 *******************************************************************************/
public class clustering {
    private static Map<Long,WeiboPoint> weiboID2PointMap;
    private static List<KmeansCluster> clusters;
    private static int clusterCounter=0;
    private static boolean isSquareErrorNaN=false;
    private static double maxClusterDistThreshold=1.0;
    private static double allCentersSquareErrorThreshold=0.00000001;
    private static int maxIteration = 100;
    private static int k;
    private static Map<Long,String> oriDatMap = new HashMap<>();
    public static void main(String args[]) throws Exception{
        long startTime = System.currentTimeMillis();
        String weibosTagSegLdaPath= "/home/xiaoming/lda/GibbsLDA++-0.2/LDA" + File.separator+"lda.vect";
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(weibosTagSegLdaPath),"UTF-8"));
        String line;
        long lineNum = 0;
        weiboID2PointMap = new HashMap<>();
        WeiboPoint point=null;
        while ((line= br.readLine())!=null){
            lineNum++;
            if(lineNum == 1){
                try{
                    point=new WeiboPoint();
                    long linkId=Long.parseLong(line);
                    point.setWeiboID(linkId);
                }catch(Exception err){
                    err.printStackTrace();
                    continue;
                }

            }else if(lineNum == 2){
                lineNum = 0;
                try{
                    String[] dimensionVals=line.split(" ");
                    double[] pointValue=new double[dimensionVals.length];
                    for (int i=0;i<dimensionVals.length;i++){
                        String strDimensionVal=dimensionVals[i];
                        double dimensionVal=Double.parseDouble(strDimensionVal);
                        pointValue[i]=dimensionVal;
                    }
                    point.setVector(pointValue);
                    weiboID2PointMap.put(point.getWeiboID(),point);
                }catch(Exception err){
                    err.printStackTrace();
                    continue;
                }

            }
        }
        curTagClustering();
        loadOriDataMap();
        for(int i =0;i < clusters.size();i++){
            KmeansCluster kl = clusters.get(i);
            List<WeiboPoint> points = kl.getPoints();
            String targeName = "cluster_"+i+".text";
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/home/xiaoming/docs/cluster/"+targeName),"UTF-8"));
            for(WeiboPoint wp: points){
                String text = oriDatMap.get(wp.getWeiboID());
                bw.write(text);
                bw.newLine();
            }
            bw.close();

        }
        System.out.println("load text lda mode use time:"+(System.currentTimeMillis()-startTime)+"-ms");
    }


    public static void loadOriDataMap() throws Exception{
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
        for(int i =0;i < titleList.size();i++){
            String title = titleList.get(i);
            String[] ss = title.split(" ");
            Long id = Long.parseLong(ss[0]);
            String text = ss[1];
            oriDatMap.put(id,text);
        }
    }

    public static void curTagClustering(){//迭代聚类
        k=100;
        int iterationIndex=0;

        initCenter();
        do {
            System.out.println("iteration:" + (iterationIndex + 1) + "...");
            classify();
        }while (updateAllCenters()&&++iterationIndex<maxIteration&&!isSquareErrorNaN);
        System.out.println("start releaseSomeResource.");
        System.out.println("start writer Cluster out.");
        System.out.println("clustering over");
    }

    //初始化聚类
    public static void initCenter(){
        clusters=new LinkedList<KmeansCluster>();
        while (clusters.size()<k){
                List<Long> weiboidSet=new LinkedList<Long>(weiboID2PointMap.keySet());
                Random rd=new Random();
                int centerIndex=rd.nextInt(weiboidSet.size());
                WeiboPoint weiboPoint=weiboID2PointMap.get(weiboidSet.get(centerIndex));
                clusters.add(new KmeansCluster(weiboPoint));
        }
        clusterCounter+=clusters.size();
        System.out.println("clusterCounter="+clusterCounter);
    }

    //开始聚类
    public static void classify(){
        for(KmeansCluster cl:clusters){
            cl.setPoints(new LinkedList<WeiboPoint>());
        }
        Set<WeiboPoint> dataSet=new HashSet<WeiboPoint>(weiboID2PointMap.values());
        for(WeiboPoint point:dataSet){
            double[] vector=point.getVector();
            double minDist=maxClusterDistThreshold;
            int minIndex=-1;
            for(int i=0;i<clusters.size();i++){
                double[] centerVector=clusters.get(i).getCenterVector();
                double dist=ClusterUtils.euclideanDistance(vector,centerVector);
                if(dist<minDist){
                    minDist=dist;
                    minIndex=i;
                }
            }
            if(minIndex==-1){
                continue;//很难聚类
            }
            point.setDist(minDist);
            clusters.get(minIndex).getPoints().add(point);
        }
        for(int i=0;i<clusters.size();i++){
            KmeansCluster kcl=clusters.get(i);
            if(kcl.getPoints().size()==0){
                clusters.remove(i);
                i--;
            }
        }
    }

    public static boolean updateAllCenters(){
        boolean isConutieIterate;
        Double allSquareError=0d;
        for(KmeansCluster cluster:clusters){
            double squareError=cluster.updateCenter();
            allSquareError+=squareError;
        }
        if(clusters.size()==0){
            System.out.println("error clusters.size()==0");
            isSquareErrorNaN=true;
            return false;
        }
        if(allSquareError.isNaN()){
            System.out.println("error allSquareError is NaN");
            isSquareErrorNaN=true;
            return false;
        }
        isSquareErrorNaN=false;
        allSquareError=allSquareError/clusters.size();
        System.out.println("this iteration SquareError="+allSquareError);
        if(allSquareError<= allCentersSquareErrorThreshold){
            isConutieIterate=false;
        }else{
            isConutieIterate=true;
        }
        return isConutieIterate;
    }
}
