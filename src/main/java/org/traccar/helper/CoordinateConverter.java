package org.traccar.helper;

public final class CoordinateConverter {

    private static final double PI = 3.14159265358979324;
    private static final double A = 6378245.0; // WGS-84 椭球体长半轴
    private static final double EE = 0.00669342162296594323; // WGS-84 椭球体第一偏心率平方

    // 检查是否在中国大陆区域
    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 135.05) return true;
        if (lat < 0.8293 || lat > 55.8271) return true;
        return false;
    }

    // 核心转换方法 (WGS84 -> GCJ-02)
    private static double[] transform(double wgLat, double wgLon) {
        // 如果不在国内，则不进行转换
        if (outOfChina(wgLat, wgLon)) {
            return new double[] { wgLat, wgLon };
        }
        
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
        
        double radLat = wgLat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
        
        // 最终的火星坐标
        double mgLat = wgLat + dLat;
        double mgLon = wgLon + dLon;
        
        return new double[] { mgLat, mgLon };
    }

    // 转换入口
    public static double[] wgs84ToGcj02(double lat, double lon) {
        return transform(lat, lon);
    }
    
    // ... 需要实现 transformLat 和 transformLon 两个辅助函数
}
