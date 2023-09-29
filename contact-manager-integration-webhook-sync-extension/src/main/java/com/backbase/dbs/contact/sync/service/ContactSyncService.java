package com.backbase.dbs.contact.sync.service;

import static java.util.Objects.isNull;

import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactBulkSyncPostRequestBody;
import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactSyncPostRequestBody;
import com.backbase.dbs.contact.sync.core.CoreBankingSystemFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactSyncService {

    private final CoreBankingSystemFacade coreBankingSystemFacade;

    public void bulkSync(ContactBulkSyncPostRequestBody contactBulkSyncPostRequestBody) {
        log.debug("Started core banking system BULK synchronization with parameter={}", contactBulkSyncPostRequestBody);
        coreBankingSystemFacade.bulkSync(contactBulkSyncPostRequestBody);
    }

    public void syncContact(ContactSyncPostRequestBody contactSyncRequest) {
        log.debug("Started core banking system synchronization with parameters={}", contactSyncRequest);
        final var action = contactSyncRequest.getAction();
        switch (action) {
            case CREATE -> syncContactCreation(contactSyncRequest);
            case UPDATE -> syncContactUpdate(contactSyncRequest);
            case DELETE -> syncContactDeletion(contactSyncRequest);
            default -> throw new UnsupportedOperationException();
        }
        log.info("Core banking system synchronization completed");
    }

    private void syncContactCreation(ContactSyncPostRequestBody contactSyncRequest) {
        log.info("Creating new contact in core banking system");
        if (isNull(contactSyncRequest.getAfter())) {
            throw new BadRequestException("Parameter 'after' is required for contact creation");
        }
        coreBankingSystemFacade.createContact(contactSyncRequest.getAfter());
        log.info("Contact creation in core banking system completed");
    }

    private void syncContactUpdate(ContactSyncPostRequestBody contactSyncRequest) {
        log.info("Updating contact in core banking system");
        if (isNull(contactSyncRequest.getBefore()) || isNull(contactSyncRequest.getAfter())) {
            throw new BadRequestException("Parameters 'before' and 'after' are required for contact update");
        }
        coreBankingSystemFacade.updateContact(contactSyncRequest.getBefore(), contactSyncRequest.getAfter());
        log.info("Contact update in core banking system completed");
    }

    private void syncContactDeletion(ContactSyncPostRequestBody contactSyncRequest) {
        log.info("Deleting contact in core banking system");
        if (isNull(contactSyncRequest.getBefore())) {
            throw new BadRequestException("Parameter 'before' is required for contact deletion");
        }
        coreBankingSystemFacade.deleteContact(contactSyncRequest.getBefore());
        log.info("Contact deletion in core banking system completed");
    }
}
