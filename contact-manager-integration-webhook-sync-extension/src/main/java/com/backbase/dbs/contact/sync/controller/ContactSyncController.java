package com.backbase.dbs.contact.sync.controller;

import static org.springframework.http.ResponseEntity.ok;

import com.backbase.dbs.contact.integration.webhook.sync.v1.api.ContactsApi;
import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactBulkSyncPostRequestBody;
import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactSyncPostRequestBody;
import com.backbase.dbs.contact.sync.service.ContactSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContactSyncController implements ContactsApi {

    private final ContactSyncService contactSyncService;

    @Override
    public ResponseEntity<Void> bulkSyncContacts(ContactBulkSyncPostRequestBody contactBulkSyncPostRequestBody) {
        contactSyncService.bulkSync(contactBulkSyncPostRequestBody);
        return ok().build();
    }

    @Override
    public ResponseEntity<Void> syncContact(ContactSyncPostRequestBody contactSyncPostRequestBody) {
        contactSyncService.syncContact(contactSyncPostRequestBody);
        return ok().build();
    }
}
