package com.learngrails.data

/**
 * Created with IntelliJ IDEA.
 * User: ydz
 * Date: 14-10-27
 * Time: 下午4:24
 * To change this template use File | Settings | File Templates.
 */
public enum WeiXinAccountType {

    COMMEND_SUBSCRIBE("普通订阅号"),
    ATTESTATION_SUBSCRIBE("认证订阅号"),
    COMMEND_SERVICE("普通服务号"),
    ATTESTATION_SERVICE("认证服务号"),
    COMMEND_ENTERPRISE("普通企业号"),
    ATTESTATION_ENTERPRISE("认证企业号")

    private String label
    private String name

    WeiXinAccountType(String label) {
        this.label = label
    }

    public String getLabel() {
        return this.label
    }
}