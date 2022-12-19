package su.codes.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ESConfig {

    @Value("${elasticsearch.host:127.0.0.1}")
    private String hostName;
    @Value("${elasticsearch.port:9200}")
    private int port;
    @Value("${elasticsearch.username:admin}")
    private String username;
    @Value("${elasticsearch.password:password}")
    private String password;

    @Bean
    public CredentialsProvider getCredentialsProvider() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
        return credentialsProvider;
    }

    @Bean
    public RestClient getRestClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(hostName, port))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.setDefaultCredentialsProvider(getCredentialsProvider()));
        // Create the low-level client
        RestClient restClient = builder.build();
        return restClient;
    }

    @Bean
    public ElasticsearchTransport getElasticsearchTransport() {
        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(getRestClient(), new JacksonJsonpMapper());
        return transport;
    }

    @Bean
    public ElasticsearchClient getElasticsearchClient() {
        // Create the API client
        return new ElasticsearchClient(getElasticsearchTransport());
    }

}
