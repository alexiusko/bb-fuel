package com.backbase.ct.bbfuel.data;

import static com.backbase.ct.bbfuel.util.CommonHelpers.getRandomFromList;
import static java.util.Arrays.asList;

import com.backbase.dbs.messages.integration.model.v1.topics.CreateTopicIntegrationRequest;
import com.backbase.dbs.messages.presentation.rest.spec.v4.messagecenter.ConversationDraftsPostRequestBody;
import com.backbase.dbs.messages.presentation.rest.spec.v4.messagecenter.DraftsPostRequestBody;
import com.backbase.dbs.messages.presentation.rest.spec.v4.messagecenter.MessageDraftsPostRequestBody;
import com.backbase.dbs.messages.presentation.rest.spec.v4.messagecenter.PresentationMessagePostRequestBody;
import com.backbase.dbs.messages.presentation.rest.spec.v4.messagecenter.SubscriptionsPostRequestBody;
import com.backbase.dbs.messages.presentation.rest.spec.v4.messagecenter.TopicsPostRequestBody;
import com.github.javafaker.Faker;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;

public class MessagesDataGenerator {

    private static Faker faker = new Faker();

    @Deprecated
    public static DraftsPostRequestBody generateDraftsPostRequestBody(List<String> topicIds) {
        return new DraftsPostRequestBody()
            .withBody(encodeString(faker.lorem().paragraph()))
            .withSubject(faker.lorem().sentence().replace(".", ""))
            .withCategory(getRandomFromList(topicIds))
            .withImportant(true);
    }

    @Deprecated
    public static ConversationDraftsPostRequestBody generateConversationDraftsPostRequestBody() {
        return new ConversationDraftsPostRequestBody()
            .withBody(encodeString(faker.lorem().paragraph()));
    }

    public static TopicsPostRequestBody generateTopicPostRequestBody(Set<String> subscribers) {
        return new TopicsPostRequestBody()
            .withName(faker.lorem().sentence(2, 2))
            .withSubscribers(subscribers);
    }

    public static PresentationMessagePostRequestBody generateMessagePostRequestBody(String topicId) {
        return new PresentationMessagePostRequestBody()
            .withSubject(faker.lorem().word())
            .withTopic(topicId)
            .withBody(encodeString(faker.lorem().paragraph()));
    }

    public static PresentationMessagePostRequestBody generateMessagePostRequestBody(String topicId,
        String... attachmentIds) {
        return generateMessagePostRequestBody(topicId).withAttachments(asList(attachmentIds));
    }

    public static MessageDraftsPostRequestBody generateMessageDraftsPostRequestBody(String topicId,
        String... attachmentIds) {
        return new MessageDraftsPostRequestBody()
            .withBody(encodeString(faker.lorem().paragraph()))
            .withSubject(faker.lorem().word())
            .withTopic(topicId)
            .withAttachments(Arrays.asList(attachmentIds));
    }

    public static SubscriptionsPostRequestBody generateSubscriptionsPostRequestBody(String internalUserId) {
        return new SubscriptionsPostRequestBody()
            .withInternalUserId(internalUserId);
    }

    public static CreateTopicIntegrationRequest generateTopicIntegrationRequest() {
        return new CreateTopicIntegrationRequest()
            .withName(faker.lorem().sentence(2, 2))
            .withExternalId(faker.idNumber().valid());
    }

    private static String encodeString(String value) {
        byte[] bodyEncoded = Base64.encodeBase64(value.getBytes());

        return new String(bodyEncoded);
    }
}
