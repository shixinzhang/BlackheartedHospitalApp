package net.sxkeji.blacksearch.beans;

import java.util.List;

/**
 * 另一种格式的莆田JSON数据
 * Created by zhangshixin on 5/3/2016.
 */
public class BlackPutianBean {

    /**
     * hospital : {"city":"泰州市","count":"3","hospital":["江苏泰州红房子医院","泰州市海陵医院","泰州海陵女子医院"]}
     */

    private List<BlackhospitalsEntity> blackhospitals;

    public void setBlackhospitals(List<BlackhospitalsEntity> blackhospitals) {
        this.blackhospitals = blackhospitals;
    }

    public List<BlackhospitalsEntity> getBlackhospitals() {
        return blackhospitals;
    }

    public static class BlackhospitalsEntity {
        /**
         * city : 泰州市
         * count : 3
         * hospital : ["江苏泰州红房子医院","泰州市海陵医院","泰州海陵女子医院"]
         */

        private HospitalEntity hospital;

        public void setHospital(HospitalEntity hospital) {
            this.hospital = hospital;
        }

        public HospitalEntity getHospital() {
            return hospital;
        }

        public static class HospitalEntity {
            private String city;
            private String count;
            private List<String> hospital;

            public void setCity(String city) {
                this.city = city;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public void setHospital(List<String> hospital) {
                this.hospital = hospital;
            }

            public String getCity() {
                return city;
            }

            public String getCount() {
                return count;
            }

            public List<String> getHospital() {
                return hospital;
            }
        }
    }
}
