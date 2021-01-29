package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.treeleaf.anydone.serviceprovider.realm.model.Participant;

import io.realm.Realm;

public class ParticipantRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final ParticipantRepo participantRepo;
    private static final String TAG = "ParticipantRepo";

    static {
        participantRepo = new ParticipantRepo();
    }

    public static ParticipantRepo getInstance() {
        return participantRepo;
    }

    public Participant getParticipantById(String participantId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Participant.class)
                    .equalTo("participantId", participantId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public Participant getParticipantByEmployeeId(String employeeId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Participant.class)
                    .equalTo("employee.assignEmployeeId", employeeId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }
}
