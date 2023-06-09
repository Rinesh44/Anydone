package com.anydone.desk.inboxdetails.inboxtimeline;

import com.treeleaf.anydone.entities.TicketProto;
import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.Inbox;
import com.anydone.desk.realm.model.Participant;

import java.util.List;

public class InboxTimelineContract {

    public interface InboxTimelineView extends BaseView {

        void addParticipantSuccess(String empId);

        void addParticipantFail(String msg);

        void getInboxByIdSuccess(TicketProto.EmployeeAssigned employeeAssigned);

        void getInboxByIdFail(String msg);

        void deleteParticipantSuccess();

        void deleteParticipantFail(String msg);

        void onConversationLeaveSuccess();

        void onConversationLeaveFail(String msg);

        void onConversationDeleteSuccess();

        void onConversationDeleteFail(String msg);

        void onMuteNotificationSuccess();

        void onMuteNotificationFail(String msg);

        void onUnMuteSuccess();

        void onUnMuteFail(String msg);

        void updateParticipantNotificationSuccess(String participantId, String notificationType);

        void updateParticipantNotificationFail(String msg);

        void convertToGroupSuccess();

        void convertToGroupFail(String msg);

    }

    public interface InboxTimelinePresenter extends Presenter<InboxTimelineContract.InboxTimelineView> {

        void addParticipants(String inboxId, List<String> employeeIds);

        void getInboxById(String inboxId);

        void deleteParticipant(String inboxId, List<Participant> participants);

        void leaveConversation(String inboxId);

        void leaveAndDeleteConversation(String inboxId);

        void muteInboxNotification(String inboxId, boolean mentions);

        void unMuteNotification(String inboxId);

        void updateParticipantNotification(String inboxId,
                                           String participantId,
                                           List<Participant> participantList, boolean mute);

        void convertToGroup(Inbox inbox, String subject);

    }
}
