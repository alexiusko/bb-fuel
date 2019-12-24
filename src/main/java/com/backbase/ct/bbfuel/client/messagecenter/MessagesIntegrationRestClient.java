package com.backbase.ct.bbfuel.client.messagecenter;

import com.backbase.ct.bbfuel.client.common.RestClient;
import com.backbase.ct.bbfuel.config.BbFuelConfiguration;
import com.backbase.dbs.messages.integration.model.v1.topics.CreateTopicIntegrationRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagesIntegrationRestClient extends RestClient {

    private final BbFuelConfiguration config;

    private static final String SERVICE_VERSION = "v1";
    private static final String ENDPOINT_TOPICS = "/topics";

    @PostConstruct
    public void init() {
        setBaseUri(config.getDbs().getMessages());
        setVersion(SERVICE_VERSION);
    }

    public Response ingestTopic(CreateTopicIntegrationRequest topicRequestBody) {
        return requestSpec()
            .contentType(ContentType.JSON)
            .body(topicRequestBody)
            .post(getPath(ENDPOINT_TOPICS));
    }

}
