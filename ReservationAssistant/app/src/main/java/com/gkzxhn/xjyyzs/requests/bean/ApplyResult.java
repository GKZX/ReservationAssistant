package com.gkzxhn.xjyyzs.requests.bean;

import java.util.List;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/8/8.
 * function:申请结果查询bean
 */

public class ApplyResult {

    /**
     * applicant : 650104198111224217
     * applyDate : 2016-08-08
     * isPass : pending
     * meetingTime : 0
     */

    private List<AppliesBean> applies;

    public List<AppliesBean> getApplies() {
        return applies;
    }

    public void setApplies(List<AppliesBean> applies) {
        this.applies = applies;
    }

    public static class AppliesBean {
        private String applicant;
        private String applyDate;
        private String name;
        private String isPass;// PENDING代表还未有人处理  审核通过是 PASSED 未通过是DENIED
        private long meetingTime;

        public String getApplicant() {
            return applicant;
        }

        public void setApplicant(String applicant) {
            this.applicant = applicant;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public String getIsPass() {
            String status = "";
            if(isPass.equals("PENDING")){
                status = "未处理";
            }else if(isPass.equals("PASSED")){
                status = "已通过";
            }else if(isPass.equals("DENIED")){
                status = "已拒绝";
            }
            return status;
        }

        public void setIsPass(String isPass) {
            this.isPass = isPass;
        }

        public long getMeetingTime() {
            return meetingTime;
        }

        public void setMeetingTime(long meetingTime) {
            this.meetingTime = meetingTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "AppliesBean{" +
                    "applicant='" + applicant + '\'' +
                    ", applyDate='" + applyDate + '\'' +
                    ", name='" + name + '\'' +
                    ", isPass='" + isPass + '\'' +
                    ", meetingTime=" + meetingTime +
                    '}';
        }
    }
}
