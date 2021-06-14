package com.anydone.desk.videocallreceive;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.treeleaf.januswebrtc.tickets.model.AssignEmployee;
import com.treeleaf.januswebrtc.tickets.model.Attachment;
import com.treeleaf.januswebrtc.tickets.model.Tickets;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Mapper {

    public static Tickets transform(com.anydone.desk.realm.model.Tickets tickets) {
        Tickets newTicket = new Tickets();
        newTicket.setId(tickets.getId());
        newTicket.setTicketId(tickets.getTicketId());
        newTicket.setTicketIndex(tickets.getTicketIndex());
        newTicket.setThreadId(tickets.getThreadId());
        newTicket.setTitle(tickets.getTitle());
        newTicket.setDescription(tickets.getDescription());
        newTicket.setTicketCategory(tickets.getTicketCategory());
        newTicket.setTicketCategoryId(tickets.getTicketCategoryId());
        newTicket.setTicketSource(tickets.getTicketSource());
        newTicket.setServiceId(tickets.getServiceId());
        newTicket.setEstimatedTime(tickets.getEstimatedTime());
        newTicket.setEstimatedTimeStamp(tickets.getEstimatedTimeStamp());
        newTicket.setCustomerType(tickets.getCustomerType());
        newTicket.setTicketType(tickets.getTicketType());
        newTicket.setCreatedAt(tickets.getCreatedAt());
        newTicket.setRelativeTime(getRelativeTime(tickets));
        newTicket.setTicketStatus(tickets.getTicketStatus());
        newTicket.setCreatedByName(tickets.getCreatedByName());
        newTicket.setCreatedByPic(tickets.getCreatedByPic());
        newTicket.setCreatedById(tickets.getCreatedById());
        newTicket.setAssignedEmployee(transform(tickets.getAssignedEmployee()));
        newTicket.setPriority(tickets.getPriority());
        return newTicket;
    }


    public static AssignEmployee transform(com.anydone.desk.realm.model.AssignEmployee assignEmployee) {
        AssignEmployee newEmployee = new AssignEmployee();
        newEmployee.setEmployeeId(assignEmployee.getEmployeeId());
        newEmployee.setAccountId(assignEmployee.getAccountId());
        newEmployee.setCreatedAt(assignEmployee.getCreatedAt());
        newEmployee.setEmployeeImageUrl(assignEmployee.getEmployeeImageUrl());
        newEmployee.setEmail(assignEmployee.getEmail());
        newEmployee.setPhone(assignEmployee.getPhone());
        newEmployee.setName(assignEmployee.getName());
        return newEmployee;
    }

    private static String getRelativeTime(com.anydone.desk.realm.model.Tickets tickets) {
        Locale localeByLanguageTag = Locale.forLanguageTag("np");
        TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(localeByLanguageTag).build();
        String relativeTime = TimeAgo.using(tickets.getCreatedAt(), messages);
        return relativeTime;
    }

    public static List<Attachment> transform(List<com.anydone.desk.realm.model.Attachment> attachmentList) {
        List<Attachment> attachments = new ArrayList<>();
        for (com.anydone.desk.realm.model.Attachment attachment : attachmentList) {
            attachments.add(transform(attachment));
        }
        return attachments;
    }

    public static Attachment transform(com.anydone.desk.realm.model.Attachment attachment) {
        Attachment attachment1 = new Attachment();
        attachment1.setId(attachment.getId());
        attachment1.setTitle(attachment.getTitle());
        attachment1.setType(attachment.getType());
        attachment1.setUrl(attachment.getUrl());
        attachment1.setCreatedAt(attachment.getCreatedAt());
        attachment1.setUpdatedAt(attachment.getUpdatedAt());
        return attachment1;
    }

}
