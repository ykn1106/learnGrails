package learngrails

import grails.transaction.Transactional
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

@Transactional
class CommonService {

    // 请求微信服务器接口（通用方法）
    private String realRequestWeiXin(String url, String requestData, String requestMethod) {
        def method
        try {
            method = (requestMethod=="post") ? new HttpPost(url):new HttpGet(url)
            if(requestData && requestData!="" && requestMethod=="post") {
                org.apache.http.HttpEntity entity = new ByteArrayEntity(requestData.getBytes("UTF-8"))
                method.setEntity(entity)
            }
            HttpResponse response = HttpClients.createDefault().execute(method)
            def result = EntityUtils.toString(response.getEntity(), "UTF-8");
            return result
        } catch(Exception e) {
            e.printStackTrace(System.out)
            return null
        } finally {
            if(method) {
                method.releaseConnection()
            }
        }
    }

    def serviceMethod() {

    }
}
