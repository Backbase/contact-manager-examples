package com.backbase.dbs.contact.sync.core;

import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactBulkSyncPostRequestBody;
import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactSyncDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NoOpCoreBankingSystemFacade implements CoreBankingSystemFacade {

    @Override
    public void createContact(ContactSyncDetails createdContact) {
        log.info("No-op implementation, skipping contact creation");
    }

    @Override
    public void updateContact(ContactSyncDetails contactBeforeUpdate, ContactSyncDetails contactAfterUpdate) {
        log.info("No-op implementation, skipping contact update");
    }

    @Override
    public void deleteContact(ContactSyncDetails deletedContact) {
        log.info("No-op implementation, skipping contact deletion");
    }

    @Override
    public void bulkSync(ContactBulkSyncPostRequestBody contactBulkSyncPostRequestBody) {
        log.info("No-op implementation, skipping bulk sync");
    }
}
