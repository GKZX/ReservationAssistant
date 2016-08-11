package com.gkzxhn.xjyyzs.requests.bean;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/11.
 * function:预约申请post所需实体类
 */

public class Apply {


    /**
     * orgCode : 0997123
     * uuid : 650104198111224217
     * applyDate : 2016-08-08
     */

    private ApplyBean apply;

    public ApplyBean getApply() {
        return apply;
    }

    public void setApply(ApplyBean apply) {
        this.apply = apply;
    }

    public class ApplyBean {
        private String orgCode;
        private String uuid;
        private String applyDate;

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        @Override
        public String toString() {
            return "ApplyBean{" +
                    "orgCode='" + orgCode + '\'' +
                    ", uuid='" + uuid + '\'' +
                    ", applyDate='" + applyDate + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Apply{" +
                "apply=" + apply.toString() +
                '}';
    }
}
