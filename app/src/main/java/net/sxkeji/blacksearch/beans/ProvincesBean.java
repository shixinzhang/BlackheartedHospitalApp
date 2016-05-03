package net.sxkeji.blacksearch.beans;

import java.util.List;

/**
 * Created by zhangshixin on 5/3/2016.
 */
public class ProvincesBean {

    /**
     * Citys : [{"Id":"d0f17d91-f06e-445c-959a-0817379325ca","Name":"钦州市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"109e4928-33c8-4949-bce1-12f00cb17984","Name":"崇左市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"38f71a71-fa64-4489-9ff7-27476712e21f","Name":"河池市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"fad6d6cf-383d-4ef4-90dd-2b020e23351b","Name":"北海市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"06641d5b-e8a5-4847-a06d-7310188548f9","Name":"梧州市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"798b8282-87ee-446d-8076-91517906d2cd","Name":"南宁市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"dae4b8d1-1a18-422e-83aa-ae8a40e63f6d","Name":"百色市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"62508bb3-ffe0-4207-a954-c45a47092b88","Name":"桂林市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"dfd657ab-3994-4cda-8b3c-dec470c53be2","Name":"来宾市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"bac46955-d379-4146-a0b3-defdaadb1a16","Name":"贺州市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"17e7c191-4e51-4aa4-bdf7-e6f79acf827c","Name":"玉林市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"689da1d2-951f-4b64-9208-e762be59bdc1","Name":"柳州市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"394d6cb1-aea1-4e4d-b547-ec192a7178ab","Name":"防城港市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"},{"Id":"b79de958-3052-4925-8d27-fbe60723362e","Name":"贵港市","ProvinceId":"167ee47c-bc85-4b2e-87a0-00af5c2cd664"}]
     * Id : 167ee47c-bc85-4b2e-87a0-00af5c2cd664
     * Name : 广西壮族自治区
     */

    private String Id;
    private String Name;
    /**
     * Id : d0f17d91-f06e-445c-959a-0817379325ca
     * Name : 钦州市
     * ProvinceId : 167ee47c-bc85-4b2e-87a0-00af5c2cd664
     */

    private List<CitysEntity> Citys;

    public void setId(String Id) {
        this.Id = Id;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setCitys(List<CitysEntity> Citys) {
        this.Citys = Citys;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public List<CitysEntity> getCitys() {
        return Citys;
    }

    public static class CitysEntity {
        private String Id;
        private String Name;
        private String ProvinceId;

        public void setId(String Id) {
            this.Id = Id;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public void setProvinceId(String ProvinceId) {
            this.ProvinceId = ProvinceId;
        }

        public String getId() {
            return Id;
        }

        public String getName() {
            return Name;
        }

        public String getProvinceId() {
            return ProvinceId;
        }
    }
}
