package org.traccar.helper;

import org.locationtech.jts.geom.Coordinate;
import com.github.intercommit.coordtransform.CoordTransform;
import com.github.intercommit.coordtransform.CoordTransformFactory;

public class CoordinateUtil {
    private static final CoordTransform transformer = CoordTransformFactory.createCoordTransform(
            CoordTransform.CoordSysType.WGS84, CoordTransform.CoordSysType.GCJ02);

    public static Coordinate wgs84ToGcj02(double lon, double lat) {
        // 检查坐标是否在中国大陆范围内，避免全球设备都被转换
        if (lon > 73.66 && lon < 135.05 && lat > 3.86 && lat < 53.55) {
            return transformer.transform(new Coordinate(lon, lat));
        }
        // 不在范围内，返回原始坐标
        return new Coordinate(lon, lat);
    }
}
