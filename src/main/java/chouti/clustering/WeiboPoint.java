package chouti.clustering;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by datamining on 2016/7/20.
 */
public class WeiboPoint {
    private long weiboID;
    private double[] vector;
    private int clusterID;//从1起
    private double dist;
    private String weibo;
    private String seg;
    private String weiboTime;
    private double hotVal;
    private boolean isInitHot;
    private String fingerprintStr;
    private Set<Long> simWeiboIDs=new HashSet<Long>();
    private Set<Long> preSimWeiboIDs=new HashSet<Long>();
    private boolean isVisited=false;
    private int index;
    private boolean isVisited2=false;
    private Map<String,Double> vectorMap;
    private boolean isHot;
    private String persons;
    private Set<WeiboPoint> others4one;

    public Set<WeiboPoint> getOthers4one() {
        return others4one;
    }

    public void setOthers4one(Set<WeiboPoint> others4one) {
        this.others4one = others4one;
    }

    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean isHot) {
        this.isHot = isHot;
    }

    public Map<String, Double> getVectorMap() {
        return vectorMap;
    }

    public void setVectorMap(Map<String, Double> vectorMap) {
        this.vectorMap = vectorMap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isVisited2() {
        return isVisited2;
    }

    public void setVisited2(boolean isVisited2) {
        this.isVisited2 = isVisited2;
    }
    //    private long pointID=-1L;

//    public long getPointID() {
//        return pointID;
//    }
//
//    public void setPointID(long pointID) {
//        this.pointID = pointID;
//    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public Set<Long> getPreSimWeiboIDs() {
        return preSimWeiboIDs;
    }

    public void setPreSimWeiboIDs(Set<Long> preSimWeiboIDs) {
        this.preSimWeiboIDs = preSimWeiboIDs;
    }

    public Set<Long> getSimWeiboIDs() {
        return simWeiboIDs;
    }

    public void setSimWeiboIDs(Set<Long> simWeiboIDs) {
        this.simWeiboIDs = simWeiboIDs;
    }

    public String getFingerprintStr() {
        return fingerprintStr;
    }

    public void setFingerprintStr(String fingerprintStr) {
        this.fingerprintStr = fingerprintStr;
    }

    public boolean isInitHot() {
        return isInitHot;
    }

    public void setInitHot(boolean isInitHot) {
        this.isInitHot = isInitHot;
    }

    public long getWeiboID() {
        return weiboID;
    }

    public void setWeiboID(long weiboID) {
        this.weiboID = weiboID;
    }

    public double[] getVector() {
        return vector;
    }

    public void setVector(double[] vector) {
        this.vector = vector;
    }

    public int getClusterID() {
        return clusterID;
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getWeiboTime() {
        return weiboTime;
    }

    public void setWeiboTime(String weiboTime) {
        this.weiboTime = weiboTime;
    }

    public String getSeg() {
        return seg;
    }

    public void setSeg(String seg) {
        this.seg = seg;
    }

    public double getHotVal() {
        return hotVal;
    }

    public void setHotVal(double hotVal) {
        this.hotVal = hotVal;
    }

    @Override
    public String toString() {
        return "WeiboPoint{" +
                "weiboID=" + weiboID +
                '}';
    }
}
