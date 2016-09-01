package com.gkzxhn.xjyyzs.requests.bean;

import java.util.List;

/**
 * author:huangzhengneng
 * email:943852572@qq.com
 * date: 2016/9/1.
 * description:搜索结果实体
 * {
    "applies": [
                {
                "apply": [
                        {
                             "_id": "57c55689a51fb41400d4e454",
                             "applyDate": "2016-09-06",
                             "feedback": {
                                        "content": "预约会见成功",
                                        "from": "M",
                                        "isPass": "PASSED",
                                        "meetingTime": "10:00",
                                        "prison": "第三监狱",
                                        "sfs": "s0997003"
                             }
                         }
                          ],
                "name": "肖君",
                "uuid": "432503199003240835"
                }
                ]
     }

 */

public class SearchResultBean {


    /**
     * apply : [{"_id":"57c55689a51fb41400d4e454","applyDate":"2016-09-06","feedback":{"content":"预约会见成功","from":"M","isPass":"PASSED","meetingTime":"10:00","prison":"第三监狱","sfs":"s0997003"}}]
     * name : 肖君
     * uuid : 432503199003240835
     */

    private List<AppliesBean> applies;

    public List<AppliesBean> getApplies() {
        return applies;
    }

    public void setApplies(List<AppliesBean> applies) {
        this.applies = applies;
    }

    public static class AppliesBean {
        private String name;
        private String uuid;
        /**
         * _id : 57c55689a51fb41400d4e454
         * applyDate : 2016-09-06
         * feedback : {"content":"预约会见成功","from":"M","isPass":"PASSED","meetingTime":"10:00","prison":"第三监狱","sfs":"s0997003"}
         */

        private List<ApplyBean> apply;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public List<ApplyBean> getApply() {
            return apply;
        }

        public void setApply(List<ApplyBean> apply) {
            this.apply = apply;
        }

        public static class ApplyBean {
            private String _id;
            private String applyDate;
            /**
             * content : 预约会见成功
             * from : M
             * isPass : PASSED
             * meetingTime : 10:00
             * prison : 第三监狱
             * sfs : s0997003
             */

            private FeedbackBean feedback;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getApplyDate() {
                return applyDate;
            }

            public void setApplyDate(String applyDate) {
                this.applyDate = applyDate;
            }

            public FeedbackBean getFeedback() {
                return feedback;
            }

            public void setFeedback(FeedbackBean feedback) {
                this.feedback = feedback;
            }

            public static class FeedbackBean {
                private String content;
                private String from;
                private String isPass;
                private String meetingTime;
                private String prison;
                private String sfs;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getFrom() {
                    return from;
                }

                public void setFrom(String from) {
                    this.from = from;
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

                public String getMeetingTime() {
                    return meetingTime;
                }

                public void setMeetingTime(String meetingTime) {
                    this.meetingTime = meetingTime;
                }

                public String getPrison() {
                    return prison;
                }

                public void setPrison(String prison) {
                    this.prison = prison;
                }

                public String getSfs() {
                    return sfs;
                }

                public void setSfs(String sfs) {
                    this.sfs = sfs;
                }

                @Override
                public String toString() {
                    return "FeedbackBean{" +
                            "content='" + content + '\'' +
                            ", from='" + from + '\'' +
                            ", isPass='" + isPass + '\'' +
                            ", meetingTime='" + meetingTime + '\'' +
                            ", prison='" + prison + '\'' +
                            ", sfs='" + sfs + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "ApplyBean{" +
                        "_id='" + _id + '\'' +
                        ", applyDate='" + applyDate + '\'' +
                        ", feedback=" + feedback.toString() +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "AppliesBean{" +
                    "name='" + name + '\'' +
                    ", uuid='" + uuid + '\'' +
                    ", apply=" + apply.size() +
                    '}';
        }
    }
}
