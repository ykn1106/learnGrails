package learngrails

import grails.converters.JSON

/**
 * 微信企业号开发接口
 *
 */
class WeiXinVipController {

    def weiXinVipService

    /**
     * userid 转 openid
     *
     */
    def convertToOpenId = {
        try {
            def client = Client.findById(params.clientId)
            if(!client){
                render([success: false, errmsg: "没有找到client!", openId: null] as JSON)
            }
            def accessToken = client?.accessToken
            def openIdResult = weiXinVipService.convertToOpenId(client, accessToken, params.userId, params.agentId)
            render(openIdResult as JSON)
        }catch (Exception e){
            log.info("=====convert to openId failed"+ e)
        }
    }
    def index() {
        render(view: "/weiXinVip/index")
    }
}
