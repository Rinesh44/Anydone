package com.treeleaf.anydone.serviceprovider.utils;

import com.google.android.gms.common.util.CollectionUtils;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SearchServiceProto;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Attachment;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.model.Label;
import com.treeleaf.anydone.serviceprovider.realm.model.Location;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.model.Receiver;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByDate;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByPriority;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByResolvedTime;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatBySource;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketStatByStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;

public final class ProtoMapper {
    private static final String TAG = "ProtoMapper";
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";

    public ProtoMapper() {
    }

    public static List<Service> transformSearchResultProto(List<SearchServiceProto.SearchResult>
                                                                   searchResultList) {
        if (searchResultList.isEmpty()) {
            throw new IllegalArgumentException(EXCEPTION_NULL_VALUE);
        }

        List<Service> serviceList = new ArrayList<>();
        for (SearchServiceProto.SearchResult searchResult : searchResultList
        ) {
            Service service = new Service();
            service.setCreatedAt(searchResult.getService().getCreatedAt());
            service.setDesc(searchResult.getService().getDesc());
            service.setName(GlobalUtils.convertCase(searchResult.getService().getName()));
            service.setServiceIconUrl(searchResult.getService().getServiceIconUrl());
            service.setServiceId(searchResult.getService().getServiceId());
            service.setServiceType(searchResult.getService().getServiceType().name());

            RealmList<ServiceAttributes> serviceAttributes =
                    getServiceAttributes(searchResult.getService());
            service.setServiceAttributesList(serviceAttributes);
            serviceList.add(service);
        }

        return serviceList;

    }


    public static RealmList<ServiceAttributes> getServiceAttributes
            (ServiceProto.Service searchResult) {
        RealmList<ServiceAttributes> serviceAttributesList = new RealmList<>();
        for (ServiceProto.ServiceAttribute attribute : searchResult.getServiceAttributesList()
        ) {
            ServiceAttributes serviceAttributes = new ServiceAttributes();
            serviceAttributes.setCreatedAt(attribute.getCreatedAt());
            serviceAttributes.setName(attribute.getName());
            serviceAttributes.setServiceAttributeType(attribute.getServiceAttributeType().name());
            serviceAttributes.setServiceId(attribute.getServiceId());
            serviceAttributes.setValue(attribute.getValue());

            serviceAttributesList.add(serviceAttributes);
        }

        return serviceAttributesList;
    }

    public static Customer transformCustomer(UserProto.Customer customerPb) {
        Customer customer1 = new Customer();
        customer1.setCustomerId(customerPb.getCustomerId());
        customer1.setFullName(customerPb.getFullName());
        customer1.setProfilePic(customerPb.getProfilePic());
        customer1.setPhone(customerPb.getPhone());
        customer1.setEmail(customerPb.getEmail());
        return customer1;
    }

    public static RealmList<Inbox> transformInbox(List<InboxProto.Inbox> inboxListPb) {
        RealmList<Inbox> inboxRealmList = new RealmList<>();
        for (InboxProto.Inbox inbox : inboxListPb
        ) {
            Inbox inbox1 = createNewInbox(inbox);
            inboxRealmList.add(inbox1);
        }
        return inboxRealmList;
    }

    public static ServiceProvider transformServiceProvider
            (UserProto.ServiceProviderProfile serviceProviderProfile) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setAccountId(serviceProviderProfile
                .getAccount().getAccountId());
        serviceProvider.setEmail(serviceProviderProfile
                .getAccount().getEmail());
        serviceProvider.setFullName(serviceProviderProfile
                .getAccount().getFullName());
        serviceProvider.setPhone(serviceProviderProfile.getAccount().getPhone());
        serviceProvider.setProfileId(serviceProviderProfile
                .getServiceProviderProfileId());
        serviceProvider.setProfilePic(serviceProviderProfile
                .getAccount().getProfilePic());
        serviceProvider.setType(serviceProviderProfile
                .getServiceProviderType().name());
        serviceProvider.setNoOfRating(serviceProviderProfile
                .getNumberOfRating());
        serviceProvider.setAvgRating(serviceProviderProfile
                .getAverageRating());

        return serviceProvider;
    }

    public static RealmList<Tags> transformTags(List<TicketProto.Team> tagListPb) {
        RealmList<Tags> tagsRealmList = new RealmList<>();
        for (TicketProto.Team ticketTagPb : tagListPb
        ) {
            Tags tags = new Tags();
            tags.setTagId(ticketTagPb.getTeamId());
            tags.setServiceId(ticketTagPb.getServiceId());
            tags.setCreatedBy(ticketTagPb.getCreatedBy().getAccount().getAccountId());
            tags.setDescription(ticketTagPb.getDescription());
            tags.setLabel(ticketTagPb.getLabel());
            tagsRealmList.add(tags);
        }
        return tagsRealmList;
    }

    public static RealmList<Attachment> transformAttachments(List<TicketProto.TicketAttachment>
                                                                     ticketAttachmentsPb) {
        RealmList<Attachment> attachmentRealmList = new RealmList<>();
        for (TicketProto.TicketAttachment attachmentPb : ticketAttachmentsPb
        ) {
            Attachment attachment = new Attachment();
            attachment.setId(attachmentPb.getId());
            attachment.setUrl(attachmentPb.getUrl());
            attachment.setUpdatedAt(attachmentPb.getUpdatedAt());
            attachment.setCreatedAt(attachmentPb.getCreatedAt());
            attachment.setTitle(attachmentPb.getTitle());
            attachment.setType(attachmentPb.getType().getNumber());
            attachmentRealmList.add(attachment);
        }
        return attachmentRealmList;
    }

    public static RealmList<Label> transformLabels(List<TicketProto.Label> labelListPb) {
        GlobalUtils.showLog(TAG, "label list count: " + labelListPb.size());
        RealmList<Label> labelRealmList = new RealmList<>();
        for (TicketProto.Label ticketLabelPb : labelListPb
        ) {
            Label label = new Label();
            label.setLabelId(ticketLabelPb.getLabelId());
            label.setName(ticketLabelPb.getName());
            label.setSpAccountId(ticketLabelPb.getSpAccountId());
            label.setServiceId(ticketLabelPb.getServiceId());
            label.setCreatedAt(ticketLabelPb.getCreatedAt());
            label.setUpdatedAt(ticketLabelPb.getUpdatedAt());
            labelRealmList.add(label);
        }
        return labelRealmList;
    }

    public static List<Label> transformLabelsManaged(List<TicketProto.Label> labelListPb) {
        final Realm realm = Realm.getDefaultInstance();
        GlobalUtils.showLog(TAG, "label list count: " + labelListPb.size());
        List<Label> labelRealmList = new ArrayList<>();
        for (TicketProto.Label ticketLabelPb : labelListPb
        ) {
            Label label = new Label();
            label.setName(ticketLabelPb.getName());
            label.setSpAccountId(ticketLabelPb.getSpAccountId());
            label.setServiceId(ticketLabelPb.getServiceId());
            label.setCreatedAt(ticketLabelPb.getCreatedAt());
            label.setUpdatedAt(ticketLabelPb.getUpdatedAt());
            label = realm.copyToRealm(label);
            labelRealmList.add(label);
        }
        return labelRealmList;
    }

    public static RealmList<Conversation> transformConversation
            (List<RtcProto.RtcMessage> messageList, boolean isReply) {
        RealmList<Conversation> conversationList = new RealmList<>();
        for (RtcProto.RtcMessage message : messageList
        ) {
            RealmList<Receiver> receiverList = new RealmList<>();
            Conversation conversation = new Conversation();
            setMessagesByType(conversation, message);
            for (RtcProto.MsgReceiver receiverPb : message.getReceiversList()
            ) {
                Receiver receiver = new Receiver();
                receiver.setReceiverId(receiverPb.getReceiverId());
                receiver.setMessageStatus(receiverPb.getRtcMessageStatus().name());
                receiver.setReceiverType(receiverPb.getReceiverActor().name());
                receiver.setSenderId(receiverPb.getAccountId());
                receiverList.add(receiver);
            }

            GlobalUtils.showLog(TAG, "transform convo()");
            conversation.setClientId(message.getClientId());
            if (message.getParentMessageId() == null) conversation.setParentId("");
            else conversation.setParentId(message.getParentMessageId());
            conversation.setConversationId(message.getRtcMessageId());
            conversation.setSenderId(message.getSenderAccountObj().getAccountId());
            conversation.setMessageType(message.getRtcMessageType().name());
            conversation.setSenderType(message.getSenderActor().name());
            conversation.setReplyCount((int) message.getNumberOfReplies());
            if (message.getSenderActor().name().equals(RtcProto.MessageActor.ANYDONE_USER_MESSAGE.name())) {
                conversation.setSenderImageUrl(message.getSenderAccountObj().getProfilePic());
                conversation.setSenderName(message.getSenderAccountObj().getFullName());
                conversation.setSenderPhone(message.getSenderAccountObj().getPhone());
                conversation.setSenderEmail(message.getSenderAccountObj().getEmail());
            } else if (message.getSenderActor().name().equals(RtcProto.MessageActor.ANYDONE_BOT_MESSAGE.name())) {
                conversation.setSenderImageUrl(message.getBotProfile().getImage());
                conversation.setSenderName(message.getBotProfile().getName());
            } else {
                conversation.setSenderImageUrl(message.getSenderAccountObj().getProfilePic());
                conversation.setSenderName(message.getSenderAccountObj().getFullName());
            }
            conversation.setSentAt(message.getSentAt());
            conversation.setSavedAt(message.getSavedAt());
            conversation.setRefId((message.getRefId()));
            if (message.hasLink()) {
                GlobalUtils.showLog(TAG, "check link text msg: ");
                if (message.getLink().getMessage() != null && !message.getLink().getMessage().isEmpty())
                    conversation.setMessage((message.getLink().getMessage()));
                else
                    conversation.setMessage((message.getLink().getTitle()));

                conversation.setLinkTitle(message.getLink().getTitle());
                conversation.setLinkDesc(message.getLink().getBody());
                conversation.setLinkImageUrl(message.getLink().getImage());
            } else if (message.hasAttachment())
                conversation.setMessage(message.getAttachment().getUrl());
            else if (message.hasImage()) {
                conversation.setMessage(message.getImage().getImages(0).getUrl());
                conversation.setImageDesc(message.getImage().getTitle());
            } else
                conversation.setMessage((message.getText().getMessage()).trim());
            conversation.setSent(true);
            conversation.setReply(isReply);
            conversation.setReceiverList(receiverList);
            conversationList.add(conversation);
        }
        return conversationList;
    }

    private static void setMessagesByType(Conversation conversation, RtcProto.RtcMessage message) {
        switch (message.getRtcMessageType().name()) {
            case "TEXT_RTC_MESSAGE":
                conversation.setMessage(message.getText().getMessage());
                break;

            case "LINK_RTC_MESSAGE":
                conversation.setMessage(message.getLink().getTitle());
                break;

            case "IMAGE_RTC_MESSAGE":
                conversation.setMessage(message.getImage().getImages(0).getUrl());
                conversation.setImageDesc(message.getImage().getTitle());
                conversation.setImageOrientation("portrait");
                break;

            case "DOC_RTC_MESSAGE":
                conversation.setMessage(message.getAttachment().getUrl());
                conversation.setFileName(message.getAttachment().getTitle());
                break;

            case "AUDIO_RTC_MESSAGE":
                break;

            case "VIDEO_RTC_MESSAGE":
                break;

            case "VIDEO_CALL_RTC_MESSAGE":
                conversation.setCallDuration(GlobalUtils.getFormattedDuration(message.getCall().getDuration()));
                conversation.setCallInitiateTime(GlobalUtils.getTimeExcludeMillis(message.getSentAt()));
                break;

            case "AUDIO_CALL_RTC_MESSAGE":
                break;

            case "UNRECOGNIZED":
                break;

            default:
                break;
        }
    }

    public static RealmList<Location> transformLocations(List<UserProto.Location> locationListPb) {
        RealmList<Location> locationList = new RealmList<>();
        for (UserProto.Location location : locationListPb
        ) {
            Location location1 = new Location();
            location1.setId(location.getLocationId());
            location1.setDefault(location.getIsDefault());
            location1.setLocationName(location.getAddress());
            location1.setLocationType(location.getLocationType().name());
            location1.setLng(location.getLongitude());
            location1.setLat(location.getLatitude());

            locationList.add(location1);
        }
        return locationList;
    }

    public static RealmList<Participant> transformParticipants(List<InboxProto.InboxParticipant>
                                                                       participantListPb) {
        RealmList<Participant> participantRealmList = new RealmList<>();
        Realm realm = Realm.getDefaultInstance();
        for (InboxProto.InboxParticipant participantPb : participantListPb
        ) {
            realm.executeTransaction(realm1 -> {
                Participant participant = realm1.createObject(Participant.class, participantPb.getParticipantId());
                participant.setRole(participantPb.getRole().name());
//            participant.setParticipantId(participantPb.getParticipantId());
                participant.setAccountType(participantPb.getUser().getAccountType().name());
                participant.setEmployee(transformEmployeeSingle(participantPb.getUser().getEmployee()));
                participantRealmList.add(participant);
            });
        }

        return participantRealmList;
    }


    public static Inbox createNewInbox(InboxProto.Inbox inboxPb) {
        Inbox newInbox = new Inbox();
        if (!CollectionUtils.isEmpty(inboxPb.getParticipantsList())) {
            newInbox.setParticipantList(transformParticipant(inboxPb.getParticipantsList()));
        }
        newInbox.setInboxId(inboxPb.getId());
//        newInbox.setServiceId(inboxPb.getServiceId());
        newInbox.setSubject(inboxPb.getSubject());
        newInbox.setCreatedByAccountType(inboxPb.getCreatedBy().getUser().getAccountType().name());
        UserProto.User account = inboxPb.getCreatedBy().getUser();
        if (account.getAccountType().name().equalsIgnoreCase(AnydoneProto.AccountType.SERVICE_PROVIDER.name())) {
            newInbox.setCreatedByUserAccountId(inboxPb.getCreatedBy().getUser()
                    .getServiceProvider().getAccount().getAccountId());
            newInbox.setCreatedByUserAccountId(account.getServiceProvider().getAccount().getAccountId());
            newInbox.setCreatedByUserEmail(account.getServiceProvider().getAccount().getEmail());
            newInbox.setCreatedByUserAccountType(account.getServiceProvider().getAccount().getAccountType().name());
            newInbox.setCreatedByUserProfilePic(account.getServiceProvider().getAccount().getProfilePic());
            newInbox.setCreatedByUserFullName(account.getServiceProvider().getAccount().getFullName());
        } else if (account.getAccountType().name().equalsIgnoreCase
                (AnydoneProto.AccountType.SERVICE_CONSUMER.name())) {
            newInbox.setCreatedByUserAccountId(inboxPb.getCreatedBy().getUser()
                    .getConsumer().getAccount().getAccountId());
            newInbox.setCreatedByUserAccountId(account.getConsumer().getAccount().getAccountId());
            newInbox.setCreatedByUserEmail(account.getConsumer().getAccount().getEmail());
            newInbox.setCreatedByUserAccountType(account.getConsumer().getAccount().getAccountType().name());
            newInbox.setCreatedByUserProfilePic(account.getConsumer().getAccount().getProfilePic());
            newInbox.setCreatedByUserFullName(account.getConsumer().getAccount().getFullName());
        } else if (account.getAccountType().name().equalsIgnoreCase
                (AnydoneProto.AccountType.EMPLOYEE.name())) {
            newInbox.setCreatedByUserAccountId(inboxPb.getCreatedBy().getUser()
                    .getEmployee().getAccount().getAccountId());
            newInbox.setCreatedByUserAccountId(account.getEmployee().getAccount().getAccountId());
            newInbox.setCreatedByUserEmail(account.getEmployee().getAccount().getEmail());
            newInbox.setCreatedByUserAccountType(account.getEmployee().getAccount().getAccountType().name());
            newInbox.setCreatedByUserProfilePic(account.getEmployee().getAccount().getProfilePic());
            newInbox.setCreatedByUserFullName(account.getEmployee().getAccount().getFullName());
        } else {
            newInbox.setCreatedByUserAccountId(inboxPb.getCreatedBy().getUser()
                    .getAnydoneUser().getAccount().getAccountId());
            newInbox.setCreatedByUserAccountId(account.getAnydoneUser().getAccount().getAccountId());
            newInbox.setCreatedByUserEmail(account.getAnydoneUser().getAccount().getEmail());
            newInbox.setCreatedByUserAccountType(account.getAnydoneUser().getAccount().getAccountType().name());
            newInbox.setCreatedByUserProfilePic(account.getAnydoneUser().getAccount().getProfilePic());
            newInbox.setCreatedByUserFullName(account.getAnydoneUser().getAccount().getFullName());
        }

        newInbox.setCreatedAt(inboxPb.getCreatedAt());
        newInbox.setUpdatedAt(inboxPb.getUpdatedAt());
        newInbox.setLastMsg(inboxPb.getMessage().getText().getMessage());
        newInbox.setLastMsgSender(inboxPb.getMessage().getSenderAccountObj().getFullName());
        if (inboxPb.getMessage().getSentAt() != 0)
            newInbox.setLastMsgDate(inboxPb.getMessage().getSentAt());
        else newInbox.setLastMsgDate(inboxPb.getCreatedAt());
        newInbox.setNotificationType(inboxPb.getNotificationType().name());
        return newInbox;
    }

    public static RealmList<Participant> transformParticipant(List<InboxProto.InboxParticipant> participantListPb) {
        RealmList<Participant> participantList = new RealmList<>();
        for (InboxProto.InboxParticipant participantPb : participantListPb
        ) {
            AssignEmployee employee = new AssignEmployee();
            employee.setAccountId(participantPb.getUser().getEmployee().getAccount().getAccountId());
            employee.setCreatedAt(participantPb.getUser().getEmployee().getCreatedAt());
            employee.setEmail(participantPb.getUser().getEmployee().getAccount().getEmail());
            employee.setEmployeeId(participantPb.getUser().getEmployee().getEmployeeProfileId());
            employee.setEmployeeImageUrl(participantPb.getUser().getEmployee().getAccount().getProfilePic());
            employee.setPhone(participantPb.getUser().getEmployee().getAccount().getPhone());
            employee.setName(participantPb.getUser().getEmployee().getAccount().getFullName());

            Participant participant = new Participant();
            participant.setAccountType(participantPb.getUser().getAccountType().name());
            participant.setEmployee(employee);
            participant.setParticipantId(participantPb.getParticipantId());
            participant.setRole(participantPb.getRole().name());
            participant.setNotificationType(participantPb.getNotificationType().name());

            participantList.add(participant);
        }
        return participantList;
    }

    public static AssignEmployee transformAssignedEmployee(
            TicketProto.EmployeeAssigned employeeProfile) {

        AssignEmployee employee = new AssignEmployee();
        employee.setAccountId(employeeProfile.getAssignedTo().getAccount().getAccountId());
        employee.setCreatedAt(employeeProfile.getAssignedAt());
        employee.setEmployeeId(employeeProfile.getAssignedTo().getEmployeeProfileId());
        employee.setEmployeeImageUrl(employeeProfile.getAssignedTo().getAccount().getProfilePic());
        employee.setName(employeeProfile.getAssignedTo().getAccount().getFullName());
        employee.setPhone(employeeProfile.getAssignedTo().getAccount().getPhone());
        employee.setEmail(employeeProfile.getAssignedTo().getAccount().getEmail());

        return employee;
    }

    public static void transformAssignedEmployeeAlt(
            TicketProto.EmployeeAssigned employeeProfile) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                Employee employee = realm.createObject(Employee.class, employeeProfile.getAssignedTo().getEmployeeProfileId());
                employee.setAccountId(employeeProfile.getAssignedTo().getAccount().getAccountId());
                employee.setCreatedAt(employeeProfile.getAssignedAt());
                employee.setEmployeeImageUrl(employeeProfile.getAssignedTo().getAccount().getProfilePic());
                employee.setName(employeeProfile.getAssignedTo().getAccount().getFullName());
                employee.setPhone(employeeProfile.getAssignedTo().getAccount().getPhone());
                employee.setEmail(employeeProfile.getAssignedTo().getAccount().getEmail());

             /*   ThreadRepo.getInstance().setAssignedEmployee(threadId,
                        employee, new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                GlobalUtils.showLog(TAG, "emp assigned");
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "failed to assign emp");
                            }
                        });*/

            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static List<AssignEmployee> transformEmployee
            (List<UserProto.EmployeeProfile> employeeList) {
        List<AssignEmployee> assignEmployeeList = new ArrayList<>();
        for (UserProto.EmployeeProfile profile : employeeList
        ) {
            AssignEmployee employee = new AssignEmployee();
            employee.setAccountId(profile.getAccount().getAccountId());
            employee.setCreatedAt(profile.getCreatedAt());
            employee.setEmail(profile.getAccount().getEmail());
            employee.setEmployeeId(profile.getEmployeeProfileId());
            employee.setEmployeeImageUrl(profile.getAccount().getProfilePic());
            employee.setName(profile.getAccount().getFullName());
            employee.setPhone(profile.getAccount().getPhone());
            assignEmployeeList.add(employee);
        }
        return assignEmployeeList;
    }

    public static AssignEmployee transformEmployeeSingle
            (UserProto.EmployeeProfile profile) {

        AssignEmployee employee = new AssignEmployee();
        employee.setAccountId(profile.getAccount().getAccountId());
        employee.setCreatedAt(profile.getCreatedAt());
        employee.setEmail(profile.getAccount().getEmail());
        employee.setEmployeeId(profile.getEmployeeProfileId());
        employee.setEmployeeImageUrl(profile.getAccount().getProfilePic());
        employee.setName(profile.getAccount().getFullName());
        employee.setPhone(profile.getAccount().getPhone());
        return employee;
    }

    public static RealmList<AssignEmployee> transformContributors
            (List<TicketProto.TicketContributor> employeeList) {
        RealmList<AssignEmployee> assignEmployeeList = new RealmList<>();
        for (TicketProto.TicketContributor profile : employeeList
        ) {
            AssignEmployee employee = new AssignEmployee();
            employee.setAccountId(profile.getEmployee().getAccount().getAccountId());
            employee.setCreatedAt(profile.getEmployee().getCreatedAt());
            employee.setEmail(profile.getEmployee().getAccount().getEmail());
            employee.setEmployeeId(profile.getEmployee().getEmployeeProfileId());
            employee.setEmployeeImageUrl(profile.getEmployee().getAccount().getProfilePic());
            employee.setName(profile.getEmployee().getAccount().getFullName());
            employee.setPhone(profile.getEmployee().getAccount().getPhone());
            assignEmployeeList.add(employee);
        }
        return assignEmployeeList;
    }

    public static TicketStatByStatus transformTicketByStatus(TicketProto.TicketStatByStatus
                                                                     ticketStatByStatusPb,
                                                             boolean multiple) {
        TicketStatByStatus ticketStatByStatus = new TicketStatByStatus();
        if (multiple) {
            ticketStatByStatus.setId(UUID.randomUUID().toString().replace("-", ""));
        } else {
            ticketStatByStatus.setId(Constants.TICKET_STAT_STATUS);
        }
        ticketStatByStatus.setClosedTickets(ticketStatByStatusPb.getClosedTickets());
        ticketStatByStatus.setResolvedTickets(ticketStatByStatusPb.getResolvedTickets());
        ticketStatByStatus.setUnResolvedTickets(ticketStatByStatusPb.getUnresolvedTickets());
        ticketStatByStatus.setNewTickets(ticketStatByStatusPb.getNewTickets());
        ticketStatByStatus.setTotalTickets(ticketStatByStatusPb.getTotalTickets());
        ticketStatByStatus.setReOpenedTickets(ticketStatByStatusPb.getReopenedTickets());
        ticketStatByStatus.setTimestamp(ticketStatByStatusPb.getTimestamp());
        ticketStatByStatus.setStatType(String.valueOf(ticketStatByStatusPb.
                getTicketStatResponseType().getNumber()));
        return ticketStatByStatus;
    }

    public static TicketStatBySource transformTicketBySource(TicketProto.TicketStatBySource
                                                                     ticketStatBySourcePb) {
        TicketStatBySource ticketStatBySource = new TicketStatBySource();
        ticketStatBySource.setId(Constants.TICKET_STAT_SOURCE);
        ticketStatBySource.setBot(ticketStatBySourcePb.getBotTickets());
        ticketStatBySource.setManual(ticketStatBySourcePb.getManualTickets());
        ticketStatBySource.setPhoneCall(ticketStatBySourcePb.getCallTickets());
        ticketStatBySource.setThirdParty(ticketStatBySourcePb.getConversationTickets());
        return ticketStatBySource;
    }

    public static TicketStatByPriority transformTicketByPriority
            (TicketProto.TicketStatByPriority
                     ticketStatByPriorityPb) {
        TicketStatByPriority ticketStatByPriority = new TicketStatByPriority();
        ticketStatByPriority.setId(Constants.TICKET_STAT_PRIORITY);
        ticketStatByPriority.setLowest(ticketStatByPriorityPb.getLowestPriorityTickets());
        ticketStatByPriority.setLow(ticketStatByPriorityPb.getLowPriorityTickets());
        ticketStatByPriority.setMedium(ticketStatByPriorityPb.getMediumPriorityTickets());
        ticketStatByPriority.setHigh(ticketStatByPriorityPb.getHighPriorityTickets());
        ticketStatByPriority.setHighest(ticketStatByPriorityPb.getHighestPriorityTickets());
        return ticketStatByPriority;
    }

    public static TicketStatByResolvedTime transformTicketByResolvedTime
            (TicketProto.TicketStatResolveTime
                     ticketStatResolveTimePb) {
        TicketStatByResolvedTime ticketStatByResolvedTime = new TicketStatByResolvedTime();
        ticketStatByResolvedTime.setId(Constants.TICKET_STAT_RESOLVED_TIME);
        ticketStatByResolvedTime.setAvg(ticketStatResolveTimePb.getAverageResolveTime());
        ticketStatByResolvedTime.setMax(ticketStatResolveTimePb.getMaximumResolveTime());
        ticketStatByResolvedTime.setMin(ticketStatResolveTimePb.getMinimumResolveTime());
        return ticketStatByResolvedTime;
    }

    public static TicketStatByDate transformTicketStatByDate
            (List<TicketProto.TicketStatByStatus>
                     ticketStatByStatusListPb) {
        TicketStatByDate ticketStatByDate = new TicketStatByDate();
        RealmList<TicketStatByStatus> ticketStatByStatusList = new RealmList<>();
        for (TicketProto.TicketStatByStatus ticketStatByStatusPb : ticketStatByStatusListPb
        ) {
            TicketStatByStatus ticketStatByStatus = transformTicketByStatus(ticketStatByStatusPb,
                    true);
            ticketStatByStatusList.add(ticketStatByStatus);
        }

        ticketStatByDate.setId(Constants.TICKET_STAT_DATE);
        ticketStatByDate.setTicketStatByStatusRealmList(ticketStatByStatusList);
        return ticketStatByDate;
    }
}