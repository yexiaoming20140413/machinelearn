package chouti.clustering;

import java.util.*;

/**
 * Created by datamining on 2016/7/20.
 */
public class KmeansCluster {
    private double[] centerVector;
    private List<WeiboPoint> points;
    private int dimensionSize;

    public KmeansCluster(WeiboPoint centerPoint) {
        this.centerVector = centerPoint.getVector();
        this.dimensionSize=centerPoint.getVector().length;
        points=new LinkedList<WeiboPoint>();
        points.add(centerPoint);

    }

    public void sortPoints(){
        Collections.sort(points, new Comparator<WeiboPoint>() {
            @Override
            public int compare(WeiboPoint o1, WeiboPoint o2) {
                double out=o1.getDist()-o2.getDist();
                if(out>0d)return 1;
                else if(out==0d)return 0;
                else return -1;
            }
        });
    }

    public double updateCenter(){
//        System.out.println("w0 points.size()=="+points.size());
        double[] newCenterVector=new double[dimensionSize];
        for(int i=0;i<dimensionSize;i++)newCenterVector[i]=0d;
//        double[] oldCenter=centerVector.clone();
        int pointSize=points.size();
        int otherPointSize=0;
        for(WeiboPoint point:points){
            double[] vect=point.getVector();
            for(int i=0;i<dimensionSize;i++){
                newCenterVector[i]+=vect[i];
            }
            Set<WeiboPoint> others=point.getOthers4one();
            if(others!=null){
                otherPointSize+=others.size();
                for(WeiboPoint wp:others){
                    double[] subVect=wp.getVector();
                    for(int i=0;i<dimensionSize;i++){
                        newCenterVector[i]+=subVect[i];
                    }
                }
            }
        }
        double squareError=0;
//        System.out.println("otherPointSize="+otherPointSize);
        pointSize+=otherPointSize;
        for(int i=0;i<dimensionSize;i++){
            if(points.size()==0){
                System.out.println("ERROR:w1 points.size()==0");
            }
            double val=newCenterVector[i]/pointSize;
            newCenterVector[i]=val;
            squareError+=Math.pow((Math.abs(val-centerVector[i])),2);
        }
        squareError=Math.sqrt(squareError);
        centerVector=newCenterVector;
//        System.out.println("points size="+points.size());
        return squareError;
    }

    public double[] getCenterVector() {
        return centerVector;
    }

    public void setCenterVector(double[] centerVector) {
        this.centerVector = centerVector;
    }

    public List<WeiboPoint> getPoints() {
        return points;
    }

    public void setPoints(List<WeiboPoint> points) {
        this.points = points;
    }
}
