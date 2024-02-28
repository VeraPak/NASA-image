import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static final String url = "https://api.nasa.gov/planetary/apod?api_key=nXZ26aSJdBH37b1ixKGEVLREFKCItxxLuckAUxaF";
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        CloseableHttpResponse response = httpClient.execute(new HttpGet(url));

        Nasa nasa = objectMapper.readValue(response.getEntity().getContent().readAllBytes(), Nasa.class);

        CloseableHttpResponse picture = httpClient.execute(new HttpGet(nasa.getHdurl()));
        String[] nasaArray = nasa.getHdurl().split("/");

        HttpEntity entity = picture.getEntity();
        if (entity != null) {
            FileOutputStream out = new FileOutputStream(nasaArray[nasaArray.length - 1]);
            entity.writeTo(out);
            out.close();
        }

        response.close();
        httpClient.close();
    }
}