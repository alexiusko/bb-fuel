package com.backbase.ct.bbfuel.client.messagecenter;

import com.backbase.ct.bbfuel.client.common.RestClient;
import com.backbase.ct.bbfuel.config.BbFuelConfiguration;
import com.backbase.dbs.messages.presentation.rest.spec.v4.messagecenter.ConversationDraftsPostRequestBody;
import com.backbase.dbs.messages.presentation.rest.spec.v4.messagecenter.DraftsPostRequestBody;
import com.backbase.dbs.messages.presentation.rest.spec.v4.messagecenter.MessageDraftsPostRequestBody;
import com.backbase.dbs.messages.presentation.rest.spec.v4.messagecenter.PresentationMessagePostRequestBody;
import com.backbase.dbs.messages.presentation.rest.spec.v4.messagecenter.TopicsPostRequestBody;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.io.File;
import java.net.URLConnection;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessagesPresentationRestClient extends RestClient {

    private final BbFuelConfiguration config;

    private static final String SERVICE_VERSION = "v4";
    private static final String MESSAGES_PRESENTATION_SERVICE = "messages-presentation-service";
    private static final String ENDPOINT_MESSAGE_CENTER = "/message-center";
    @Deprecated
    private static final String ENDPOINT_DRAFTS = ENDPOINT_MESSAGE_CENTER + "/drafts";
    @Deprecated
    private static final String ENDPOINT_SEND_DRAFT_REQUEST = ENDPOINT_DRAFTS + "/%s/send-draft-request";
    @Deprecated
    private static final String ENDPOINT_CONVERSATIONS = ENDPOINT_MESSAGE_CENTER + "/conversations";
    @Deprecated
    private static final String ENDPOINT_CONVERSATION_DRAFTS = ENDPOINT_CONVERSATIONS + "/%s/drafts";
    private static final String ENDPOINT_TOPICS = ENDPOINT_MESSAGE_CENTER + "/topics";
    private static final String ENDPOINT_MESSAGES = ENDPOINT_MESSAGE_CENTER + "/messages";
    private static final String ENDPOINT_ATTACHMENTS = ENDPOINT_MESSAGE_CENTER + "/attachments";
    private static final String ENDPOINT_MESSAGES_DRAFTS = ENDPOINT_MESSAGES + "/drafts";


    @PostConstruct
    public void init() {
        setBaseUri(config.getPlatform().getGateway());
        setVersion(SERVICE_VERSION);
        setInitialPath(MESSAGES_PRESENTATION_SERVICE);
    }

    @Deprecated
    public Response postDraft(DraftsPostRequestBody body) {
        return requestSpec()
            .contentType(ContentType.JSON)
            .body(body)
            .post(getPath(ENDPOINT_DRAFTS));
    }

    @Deprecated
    public Response sendDraftRequest(String draftId) {
        return requestSpec()
            .contentType(ContentType.JSON)
            .body("{}")
            .post(String.format(getPath(ENDPOINT_SEND_DRAFT_REQUEST), draftId));
    }

    @Deprecated
    public Response postConversationDraft(ConversationDraftsPostRequestBody draft, String conversationId) {
        return requestSpec()
            .contentType(ContentType.JSON)
            .body(draft)
            .post(String.format(getPath(ENDPOINT_CONVERSATION_DRAFTS), conversationId));
    }

    @Deprecated
    public Response getConversations() {
        return requestSpec()
            .contentType(ContentType.JSON)
            .get(getPath(ENDPOINT_CONVERSATIONS));
    }

    public Response postTopic(TopicsPostRequestBody topicsPostRequestBody) {
        return requestSpec()
            .contentType(ContentType.JSON)
            .body(topicsPostRequestBody)
            .post(getPath(ENDPOINT_TOPICS));
    }

    public Response createMessage(PresentationMessagePostRequestBody requestBody) {
        return requestSpec()
            .body(requestBody)
            .post(getPath(ENDPOINT_MESSAGES));
    }

    public Response addAttachments(File file) {
        String mimeType = URLConnection.getFileNameMap().getContentTypeFor(file.getName());
        return requestSpec()
            .contentType("multipart/form-data")
            .multiPart("attachment", file, mimeType == null ? "text/plain" : mimeType)
            .post(getPath(ENDPOINT_ATTACHMENTS));
    }

    public Response createMessagesDraft(MessageDraftsPostRequestBody requestBody) {
        return requestSpec()
            .body(requestBody)
            .post(getPath(ENDPOINT_MESSAGES_DRAFTS));
    }

}
