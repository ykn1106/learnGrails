package learngrails

import com.learngrails.data.WeiXinAccountType

class Client {
    String name
    String appid
    String appSecret
    String accessToken
    WeiXinAccountType type
    static constraints = {

    }
}
