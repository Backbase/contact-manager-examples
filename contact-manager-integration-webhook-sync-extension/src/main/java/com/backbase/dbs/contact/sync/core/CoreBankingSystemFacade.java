package com.backbase.dbs.contact.sync.core;

import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactBulkSyncPostRequestBody;
import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactSyncDetails;

public interface CoreBankingSystemFacade {

    void createContact(ContactSyncDetails createdContact);

    void updateContact(ContactSyncDetails contactBeforeUpdate, ContactSyncDetails contactAfterUpdate);

    void deleteContact(ContactSyncDetails deletedContact);

    void bulkSync(ContactBulkSyncPostRequestBody contactBulkSyncPostRequestBody);
}
