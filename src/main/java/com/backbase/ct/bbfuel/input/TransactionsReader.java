package com.backbase.ct.bbfuel.input;

import com.backbase.ct.bbfuel.data.CommonConstants;
import com.backbase.ct.bbfuel.util.ParserUtil;
import com.backbase.integration.transaction.external.rest.spec.v2.transactions.TransactionsPostRequestBody;
import com.github.javafaker.Faker;
import org.apache.commons.lang.time.DateUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.backbase.ct.bbfuel.util.CommonHelpers.generateRandomNumberInRange;
import static com.backbase.ct.bbfuel.util.CommonHelpers.getRandomFromList;
import static java.util.Arrays.asList;

public class TransactionsReader extends BaseReader {

    private static Faker faker = new Faker();

    public TransactionsPostRequestBody loadSingle(String externalArrangementId) {
        return getRandomFromList(load(globalProperties.getString(CommonConstants.PROPERTY_TRANSACTIONS_DATA_JSON)))
                .withId(UUID.randomUUID().toString())
                .withArrangementId(externalArrangementId)
                .withBookingDate(DateUtils.addDays(new Date(), generateRandomNumberInRange(-180, 0)))
                .withDescription(faker.lorem().characters(10));

    }

    private List<TransactionsPostRequestBody> load(String uri) {
        List<TransactionsPostRequestBody> transactions;

        try {
            TransactionsPostRequestBody[] parsedTransactions = ParserUtil.convertJsonToObject(uri, TransactionsPostRequestBody[].class);
            transactions = asList(parsedTransactions);
        } catch(IOException e) {
            logger.error("Failed parsing file with Transactions", e);
            throw new InvalidInputException(e.getMessage(), e);
        }
        return transactions;
    }
}
