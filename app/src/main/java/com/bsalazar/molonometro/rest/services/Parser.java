package com.bsalazar.molonometro.rest.services;

import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.ContactJson;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.json.UserJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Borja on 29/10/2016.
 */

public class Parser {

    public static User parseUser(UserJson userJson) {
        User user = new User();

        user.setUserID(userJson.getUserID());
        user.setName(userJson.getName());
        user.setPhone(userJson.getPhone());
        user.setState(userJson.getState());
        user.setImageBase64(userJson.getImage());

        return user;
    }

    public static Contact parseContact(ContactJson contactJson) {
        Contact contact = new Contact();

        contact.setUserID(contactJson.getUserID());
        contact.setName(contactJson.getName());
        contact.setPhone(contactJson.getPhone());
        contact.setState(contactJson.getState());
        contact.setImageBase64(contactJson.getImage());
        contact.setInApp(contactJson.isInApp());

        return contact;
    }


    public static Group parseGroup(GroupJson groupJson) {
        Group group = new Group();
        group.setId(groupJson.getGroupID());
        group.setName(groupJson.getName());
        group.setImageBase64(groupJson.getImage());

        group.setParticipants(new ArrayList<Participant>());

        return group;
    }

    public static ArrayList<Participant> parseParticipants(ArrayList<Participant> participantsJson) {
        ArrayList<Participant> participants = new ArrayList<>();

        for (Participant participantJson : participantsJson) {

            Participant participant = new Participant();
            participant.setUserID(participantJson.getUserID());

            if (participantJson.getUserID() == Variables.User.getUserID()) {
                participant.setImageBase64(Variables.User.getImageBase64());
                participant.setName(Variables.User.getName());
                participant.setPhone(Variables.User.getPhone());
                participant.setState(Variables.User.getState());

            } else
                for (Contact contact : Variables.contactsWithApp)
                    if (contact.getUserID() == participantJson.getUserID()) {
                        participant.setImageBase64(contact.getImageBase64());
                        participant.setName(contact.getName());
                        participant.setPhone(contact.getPhone());
                        participant.setState(contact.getState());
                    }

            participant.setMolopuntos(participantJson.getMolopuntos());
            participant.setAdmin(participantJson.isAdmin());

            participants.add(participant);
        }

        return participants;
    }
}
