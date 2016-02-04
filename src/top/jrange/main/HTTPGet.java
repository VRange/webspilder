package top.jrange.main;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.Connection;

/**
 * Created by lenovo on 2016-02-04.
 */
public class HTTPGet {
    public final static void getByString(String url, Connection connection) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try{
            HttpGet httpGet = new HttpGet(url);
            System.out.println("executing request : " + httpGet.getURI());
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    int status = httpResponse.getStatusLine().getStatusCode();
                    if(status >= 200 && status < 300){
                        HttpEntity httpEntity = httpResponse.getEntity();
                        return httpEntity != null ? EntityUtils.toString(httpEntity) : null;
                    } else {
                        throw new ClientProtocolException("Unecpected response status: " + status);
                    }
                }
            };
            String responseBody = httpClient.execute(httpGet, responseHandler);

            parsePage.parseFromString(responseBody, connection);
        } finally {
            httpClient.close();
        }
    }
}
