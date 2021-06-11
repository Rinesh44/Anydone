package com.anydone.desk.videocallreceive;

import com.treeleaf.januswebrtc.tickets.model.AssignEmployee;
import com.treeleaf.januswebrtc.tickets.model.Tickets;

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
        newEmployee.setEmployeeId(newEmployee.getEmployeeId());
        newEmployee.setAccountId(newEmployee.getAccountId());
        newEmployee.setCreatedAt(newEmployee.getCreatedAt());
        newEmployee.setEmployeeImageUrl(newEmployee.getEmployeeImageUrl());
        newEmployee.setEmail(newEmployee.getEmail());
        newEmployee.setPhone(newEmployee.getPhone());
        newEmployee.setName(newEmployee.getName());
        return newEmployee;
    }


}
