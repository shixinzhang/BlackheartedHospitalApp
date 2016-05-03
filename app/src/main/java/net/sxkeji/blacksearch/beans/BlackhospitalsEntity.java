package net.sxkeji.blacksearch.beans;

import java.util.List;

/**
 * Created by zhangshixin on 5/3/2016.
 */
public  class BlackhospitalsEntity {
    /**
     * city : 上海市
     * name : 上海市闵行区中医院
     * tel : +86 21 5187 6888
     * homepage : www.tcmmh.com
     */

    private List<HospitalEntity> hospital;

    public void setHospital(List<HospitalEntity> hospital) {
        this.hospital = hospital;
    }

    public List<HospitalEntity> getHospital() {
        return hospital;
    }

    public static class HospitalEntity {
        private String city;
        private String name;
        private String tel;
        private String homepage;

        public void setCity(String city) {
            this.city = city;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public void setHomepage(String homepage) {
            this.homepage = homepage;
        }

        public String getCity() {
            return city;
        }

        public String getName() {
            return name;
        }

        public String getTel() {
            return tel;
        }

        public String getHomepage() {
            return homepage;
        }
    }
}
