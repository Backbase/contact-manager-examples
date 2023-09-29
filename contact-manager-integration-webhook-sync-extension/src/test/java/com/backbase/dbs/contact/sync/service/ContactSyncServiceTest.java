package com.backbase.dbs.contact.sync.service;

import static com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactSyncAction.CREATE;
import static com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactSyncAction.DELETE;
import static com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactSyncAction.UPDATE;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactBulkSyncPostRequestBody;
import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactSyncDetails;
import com.backbase.dbs.contact.integration.webhook.sync.v1.model.ContactSyncPostRequestBody;
import com.backbase.dbs.contact.sync.core.CoreBankingSystemFacade;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactSyncServiceTest {

    @Mock
    private CoreBankingSystemFacade coreBankingSystemFacade;
    @Mock
    private ContactSyncPostRequestBody contactSyncPostRequestBody;
    @Mock
    private ContactSyncDetails contactSyncDetailsBeforeAction;
    @Mock
    private ContactSyncDetails contactSyncDetailsAfterAction;
    @InjectMocks
    private ContactSyncService contactSyncService;

    @Nested
    class SynchronizeContactCreationTest {

        @Test
        void shouldSynchronizeContactCreation() {
            doNothing().when(coreBankingSystemFacade).createContact(any());
            when(contactSyncPostRequestBody.getAction()).thenReturn(CREATE);
            when(contactSyncPostRequestBody.getAfter()).thenReturn(contactSyncDetailsAfterAction);

            contactSyncService.syncContact(contactSyncPostRequestBody);

            verify(coreBankingSystemFacade).createContact(same(contactSyncDetailsAfterAction));
        }

        @Test
        void shouldThrowExceptionOnSynchronizeContactCreationWithoutContactDetails() {
            when(contactSyncPostRequestBody.getAction()).thenReturn(CREATE);

            assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> contactSyncService.syncContact(contactSyncPostRequestBody))
                .withMessage("Parameter 'after' is required for contact creation");

            verifyNoInteractions(coreBankingSystemFacade);
        }
    }

    @Nested
    class SynchronizeContactUpdateTest {

        @Test
        void shouldSynchronizeContactUpdate() {
            doNothing().when(coreBankingSystemFacade).updateContact(any(), any());
            when(contactSyncPostRequestBody.getAction()).thenReturn(UPDATE);
            when(contactSyncPostRequestBody.getAfter()).thenReturn(contactSyncDetailsAfterAction);
            when(contactSyncPostRequestBody.getBefore()).thenReturn(contactSyncDetailsBeforeAction);

            contactSyncService.syncContact(contactSyncPostRequestBody);

            verify(coreBankingSystemFacade).updateContact(same(contactSyncDetailsBeforeAction),
                same(contactSyncDetailsAfterAction));
        }

        @Test
        void shouldThrowExceptionOnSynchronizeContactUpdateWithoutAfterContactDetails() {
            when(contactSyncPostRequestBody.getBefore()).thenReturn(contactSyncDetailsBeforeAction);

            shouldThrowExceptionOnSynchronizeContactUpdateWithoutContactDetails();
        }

        @Test
        void shouldThrowExceptionOnSynchronizeContactUpdateWithoutContactDetails() {
            when(contactSyncPostRequestBody.getAction()).thenReturn(UPDATE);

            assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> contactSyncService.syncContact(contactSyncPostRequestBody))
                .withMessage("Parameters 'before' and 'after' are required for contact update");

            verifyNoInteractions(coreBankingSystemFacade);
        }
    }

    @Nested
    class SynchronizeContactDeletionTest {

        @Test
        void shouldSynchronizeContactDeletion() {
            doNothing().when(coreBankingSystemFacade).deleteContact(any());
            when(contactSyncPostRequestBody.getAction()).thenReturn(DELETE);
            when(contactSyncPostRequestBody.getBefore()).thenReturn(contactSyncDetailsBeforeAction);

            contactSyncService.syncContact(contactSyncPostRequestBody);

            verify(coreBankingSystemFacade).deleteContact(same(contactSyncDetailsBeforeAction));
        }

        @Test
        void shouldThrowExceptionOnSynchronizeContactDeletionWithoutContactDetails() {
            when(contactSyncPostRequestBody.getAction()).thenReturn(DELETE);

            assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> contactSyncService.syncContact(contactSyncPostRequestBody))
                .withMessage("Parameter 'before' is required for contact deletion");

            verifyNoInteractions(coreBankingSystemFacade);
        }
    }

    @Nested
    class BulkSync {

        @Test
        void shouldSyncBulk() {
            final var mockRequest = mock(ContactBulkSyncPostRequestBody.class);

            doNothing().when(coreBankingSystemFacade).bulkSync(mockRequest);
            contactSyncService.bulkSync(mockRequest);

            verify(coreBankingSystemFacade).bulkSync(mockRequest);
        }

    }
}
