package com.backbase.ct.bbfuel.configurator;

import static com.backbase.ct.bbfuel.data.CommonConstants.PROPERTY_MESSAGES_MAX;
import static com.backbase.ct.bbfuel.data.CommonConstants.PROPERTY_MESSAGES_MIN;
import static com.backbase.ct.bbfuel.data.CommonConstants.PROPERTY_MESSAGE_TOPICS_MAX;
import static com.backbase.ct.bbfuel.data.CommonConstants.PROPERTY_MESSAGE_TOPICS_MIN;
import static com.backbase.ct.bbfuel.data.MessagesDataGenerator.generateSubscriptionsPostRequestBody;
import static com.backbase.ct.bbfuel.data.MessagesDataGenerator.generateTopicIntegrationRequest;
import static org.apache.http.HttpStatus.SC_OK;

import com.backbase.ct.bbfuel.client.common.LoginRestClient;
import com.backbase.ct.bbfuel.client.messagecenter.MessagesIntegrationRestClient;
import com.backbase.ct.bbfuel.client.messagecenter.MessagesPresentationRestClient;
import com.backbase.ct.bbfuel.client.user.UserPresentationRestClient;
import com.backbase.ct.bbfuel.data.MessagesDataGenerator;
import com.backbase.ct.bbfuel.service.LegalEntityService;
import com.backbase.ct.bbfuel.util.CommonHelpers;
import com.backbase.ct.bbfuel.util.GlobalProperties;
import com.backbase.dbs.messages.integration.model.v1.topics.CreateTopicIntegrationRequest;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagesConfigurator {

    private static GlobalProperties globalProperties = GlobalProperties.getInstance();
    private final LoginRestClient loginRestClient;
    private final MessagesPresentationRestClient messagesPresentationRestClient;
    private final MessagesIntegrationRestClient messagesIntegrationRestClient;
    private final LegalEntityService legalEntityService;
    private final UserPresentationRestClient userPresentationRestClient;

    public void ingestConversations(String externalUserId) {
        int howManyMessages = CommonHelpers.generateRandomNumberInRange(globalProperties.getInt(PROPERTY_MESSAGES_MIN),
            globalProperties.getInt(PROPERTY_MESSAGES_MAX));

        int howManyTopics = CommonHelpers.generateRandomNumberInRange(
            globalProperties.getInt(PROPERTY_MESSAGE_TOPICS_MIN), globalProperties.getInt(PROPERTY_MESSAGE_TOPICS_MAX));

        loginRestClient.loginBankAdmin();

        IntStream.range(0, howManyTopics).forEach(number -> {
            CreateTopicIntegrationRequest requestBody = generateTopicIntegrationRequest();
            String topicId = messagesIntegrationRestClient.ingestTopic(requestBody)
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("id");
            log.info("Topic ingested with id [{}]", requestBody.getName());

            loginRestClient.loginBankAdmin();
            String internalUserId = userPresentationRestClient.getUserByExternalId(externalUserId).getId();
            messagesPresentationRestClient
                .postSubscription(generateSubscriptionsPostRequestBody(internalUserId), topicId);
            log.info("User [{}] was subscribed to topic with id [{}]", externalUserId, topicId);

//            TopicsPostRequestBody topicRequestBody = MessagesDataGenerator
//                .generateTopicPostRequestBody(singleton(externalUserId));
//            String topicId = messagesPresentationRestClient.postTopic(topicRequestBody)
//                .then()
//                .statusCode(SC_ACCEPTED)
//                .extract()
//                .path("id");
//            log.info("Topic ingested with id [{}] for subscriber [{}]", topicRequestBody.getName(), externalUserId);
//            topicIds.add(topicId);

            IntStream.range(0, howManyMessages).forEach(messageNumber -> {
//                loginRestClient.login(externalUserId, externalUserId);
                messagesPresentationRestClient
                    .createMessage(MessagesDataGenerator.generateMessagePostRequestBody(topicId));
            });
            log.info("Message ingested into topic [{}] by user [{}]", topicId, externalUserId);
        });

//            DraftsPostRequestBody draftsPostRequestBody = MessagesDataGenerator.generateDraftsPostRequestBody(topicIds);
//            String draftId = messagesPresentationRestClient.postDraft(draftsPostRequestBody)
//                .then()
//                .statusCode(SC_ACCEPTED)
//                .extract()
//                .as(DraftsPostResponseBody.class)
//                .getId();
//
//            messagesPresentationRestClient.sendDraftRequest(draftId)
//                .then()
//                .statusCode(SC_ACCEPTED);

//            loginRestClient.loginBankAdmin();
//            String conversationId = messagesPresentationRestClient.getConversations()
//                .then()
//                .statusCode(SC_OK)
//                .extract()
//                .as(ConversationsGetResponseBody[].class)[0]
//                .getId();

//            String conversationDraftId = messagesPresentationRestClient
//                .postConversationDraft(MessagesDataGenerator.generateConversationDraftsPostRequestBody(),
//                    conversationId)
//                .then()
//                .statusCode(SC_ACCEPTED)
//                .extract()
//                .as(ConversationDraftsPostResponseBody.class)
//                .getId();
//
//            messagesPresentationRestClient.sendDraftRequest(conversationDraftId)
//                .then()
//                .statusCode(SC_ACCEPTED);

    }
}
