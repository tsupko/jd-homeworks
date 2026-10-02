package edu.tsupko;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tsupko.catfacts.CatFact;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

public class CatFactsApp {

    private static final String CAT_FACTS_URL =
            "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) throws IOException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(30000)
                .setRedirectsEnabled(false)
                .build();

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {

            HttpGet request = new HttpGet(CAT_FACTS_URL);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    System.err.println("Request failed with status: " + statusCode);
                    return;
                }

                ObjectMapper objectMapper = new ObjectMapper();
                List<CatFact> catFacts = objectMapper.readValue(
                        response.getEntity().getContent(),
                        new TypeReference<>() {
                        }
                );

                List<CatFact> filtered = catFacts.stream()
                        .filter(fact -> fact.getUpvotes() != null)
                        .toList();

                System.out.println("Total facts: " + catFacts.size());
                System.out.println("Facts with upvotes: " + filtered.size());
                System.out.println("---");

                for (CatFact fact : filtered) {
                    System.out.printf("[%s] %s (by %s, upvotes: %d)%n",
                            fact.getId(), fact.getText(), fact.getUser(), fact.getUpvotes());
                }
            }
        }
    }
}
