package learngrails

import com.learngrails.data.WeiXinAccountType
import grails.converters.JSON
import grails.transaction.Transactional
import com.learngrails.common.Constant
import org.omg.CORBA.MARSHAL
import sun.applet.resources.MsgAppletViewer

@Transactional
class WeiXinVipService {

    def commonService

    /**
     * userid转换成openid接口
     * {
     "errcode": 0,
     "errmsg": "ok",
     "openid": "oDOGms-6yCnGrRovBj2yHij5JL6E",
     "appid":"wxf874e15f78cc84a7"
     }
     */
    def convertToOpenId(Client client, String accessToken, String userId, String agentId){
        def result = [success:false, errcode: null, errmsg: "转换失败!", openId: null] as Map
        def url="https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_openid?access_token=${accessToken}"
        def data = "{\"userid\": \"${userId}\",\"agentid\": ${agentId}}"
        log.info("convertToOpenId post data:" + data)
        def requestResult = commonService.realRequestWeiXin(url, data, "post")
        def jsonObj = (requestResult) ? this.reRequest(url, data, "post", requestResult, client) : null
        log.info("convertToOpenId result: ${jsonObj}")
        if(jsonObj?.errcode == 0){
            result.success = true
            result.errcode = 0
            result.errmsg = "ok"
            result.openId = jsonObj.openid
        }else{
            result.errcode = jsonObj.errcode
            result.errmsg = jsonObj.errmsg
        }
        return  result
    }

    // 如果创建返回由access_token过期而创建失败的结果，则重新向微信申请access_token值，并重新申请结果（通用方法）
    private Map reRequest(String url, String requestData, String requestMethod, String requestResult, Client client) {
        def jsonResult = JSON.parse(requestResult)
        if(jsonResult?.errcode==40014 || jsonResult?.errcode==42001 || jsonResult?.errcode==40001) {
            def accessToken = this.accessToken(client)
            def startUrl = url.substring(0, url.indexOf("access_token="))
            def endUrl = url.substring(url.indexOf("access_token=")+13)
            if(endUrl.indexOf("&") >= 0) {
                endUrl = accessToken+endUrl.substring(endUrl.indexOf("&"))
            }
            else {
                endUrl = accessToken
            }
            url = startUrl+"access_token="+endUrl
            def result = commonService.realRequestWeiXin(url, requestData, requestMethod)
            jsonResult = (result) ? JSON.parse(result):null
        }
        return jsonResult as Map
    }

    /**
     * 获取accessToken
     * @param client
     */
    private def accessToken(Client client){
        def url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"+"&appid="+client?.appid+"&secret="+client?.appSecret
        if(this.isEnterprise(client.type)){
            url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+client?.appid+"&corpsecret="+client?.appSecret
        }
        def result = commonService.realRequestWeiXin(url, null, "get")
        def jsonObj = JSON.parse(result)
        log.info("weixin getAccessTokenResult====" + jsonObj)
        if(jsonObj.access_token) {
            synchronized (Constant.accessTokenLock) {
                client.accessToken = jsonObj.access_token
                client.save(flush: true, failOnError: true)
            }
            return client.accessToken
        } else { return null }
    }

    /**
     * 判断是否是企业号
     * @param type
     * @return
     */
    private def boolean isEnterprise(WeiXinAccountType  type) {
        if(type == WeiXinAccountType.ATTESTATION_ENTERPRISE || type == WeiXinAccountType.COMMEND_ENTERPRISE) {
            return true
        }
        return false
    }

    def serviceMethod() {

    }
}
