package com.treeleaf.anydone.serviceprovider.utils;

import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.SearchServiceProto;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;
import com.treeleaf.anydone.serviceprovider.realm.model.Location;
import com.treeleaf.anydone.serviceprovider.realm.model.Receiver;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

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

    public static ServiceProvider transformServiceProvider
            (OrderServiceProto.ServiceOrder serviceOrder) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setAccountId(serviceOrder.getServiceProviderAccount()
                .getAccount().getAccountId());
        serviceProvider.setEmail(serviceOrder.getServiceProviderAccount()
                .getAccount().getEmail());
        serviceProvider.setFullName(serviceOrder.getServiceProviderAccount()
                .getAccount().getFullName());
        serviceProvider.setPhone(serviceOrder.getServiceProviderAccount().getAccount().getPhone());
        serviceProvider.setProfileId(serviceOrder.getServiceProviderAccount()
                .getServiceProviderProfileId());
        serviceProvider.setProfilePic(serviceOrder.getServiceProviderAccount()
                .getAccount().getProfilePic());
        serviceProvider.setType(serviceOrder.getServiceProviderAccount()
                .getServiceProviderType().name());
        serviceProvider.setNoOfRating(serviceOrder.getServiceProviderAccount()
                .getNumberOfRating());
        serviceProvider.setAvgRating(serviceOrder.getServiceProviderAccount()
                .getAverageRating());

        return serviceProvider;
    }

    public static RealmList<Conversation> transformConversation
            (List<RtcProto.RtcMessage> messageList) {
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
            conversation.setConversationId(message.getRtcMessageId());
            conversation.setSenderId(message.getSenderAccountObj().getAccountId());
            conversation.setMessageType(message.getRtcMessageType().name());
            conversation.setSenderType(message.getSenderActor().name());
            conversation.setSenderImageUrl(message.getSenderAccountObj().getProfilePic());
            conversation.setSenderName(message.getSenderAccountObj().getFullName());
            conversation.setSentAt(message.getSentAt());
            conversation.setSavedAt(message.getSavedAt());
            conversation.setRefId(Long.parseLong(message.getRefId()));
            conversation.setSent(true);
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
                conversation.setCallInitiateTime(GlobalUtils.getTime(message.getSentAt()));
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

}
