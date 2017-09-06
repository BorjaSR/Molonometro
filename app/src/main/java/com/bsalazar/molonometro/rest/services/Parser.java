package com.bsalazar.molonometro.rest.services;

import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.entities.LastEvent;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.ContactJson;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.json.LastEventJson;
import com.bsalazar.molonometro.rest.json.UserJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        group.setFirebaseTopic(groupJson.getFirebaseTopic());

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            group.setLastUpdate(dateFormat.parse(groupJson.getLastUpdate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (groupJson.getLastEvent() != null)
            group.setLastEvent(parseLastEvent(groupJson.getLastEvent()));

        group.setParticipants(new ArrayList<Participant>());

        return group;
    }

    private static LastEvent parseLastEvent(LastEventJson lastEventJson) {
        LastEvent lastEvent = new LastEvent();

        if (lastEventJson.getLastUpdate() == null)
            return null;

        lastEvent.setCommentID(lastEventJson.getCommentID());
        lastEvent.setUserID(lastEventJson.getUserID());
        lastEvent.setDestinationUserID(lastEventJson.getDestinationUserID());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            lastEvent.setLastUpdate(dateFormat.parse(lastEventJson.getLastUpdate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (lastEventJson.getUserID() == Variables.User.getUserID())
            lastEvent.setUserName(Variables.User.getName());
        else {
            for (Contact contact : Variables.contactsWithApp)
                if (contact.getUserID() == lastEventJson.getUserID())
                    lastEvent.setUserName(contact.getName());
        }

        if (lastEventJson.getDestinationUserID() == Variables.User.getUserID())
            lastEvent.setDestinationUserName(Variables.User.getName());
        else {
            for (Contact contact : Variables.contactsWithApp)
                if (contact.getUserID() == lastEventJson.getDestinationUserID())
                    lastEvent.setDestinationUserName(contact.getName());
        }

        return lastEvent;
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

    public static ArrayList<Comment> parseComments(ArrayList<Comment> commentsJson) {
        ArrayList<Comment> comments = new ArrayList<>();

        for (Comment commentJson : commentsJson) {
            Comment comment = new Comment();

            comment.setCommentID(commentJson.getCommentID());
            comment.setUserID(commentJson.getUserID());
            comment.setDestinationUserID(commentJson.getDestinationUserID());
            comment.setHasAnswers(commentJson.isHasAnswers());
            comment.setText(commentJson.getText());
            comment.setImage(commentJson.getImage());

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                comment.setDate(dateFormat.parse(commentJson.getLastUpdate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (commentJson.getComments() != null)
                comment.setComments(commentJson.getComments());
            else
                comment.setComments(new ArrayList<Integer>());

            if (commentJson.getLikes() != null)
                comment.setLikes(commentJson.getLikes());
            else
                comment.setLikes(new ArrayList<Integer>());

            if (commentJson.getUserID() == Variables.User.getUserID()) {
                comment.setUserName(Variables.User.getName());
                comment.setUserImage(Variables.User.getImageBase64());
                comment.setDestinationUserName(getContactByID(commentJson.getDestinationUserID()).getName());
            } else {
                Contact contact = getContactByID(commentJson.getUserID());
                if (contact != null) {
                    comment.setUserName(contact.getName());
                    comment.setUserImage(contact.getImageBase64());

                    if (commentJson.getDestinationUserID() == Variables.User.getUserID())
                        comment.setDestinationUserName(Variables.User.getName());
                    else
                        comment.setDestinationUserName(getContactByID(commentJson.getDestinationUserID()).getName());

                }
            }

            comments.add(comment);
        }

        return comments;
    }

    public static ArrayList<Comment> parseReply(ArrayList<Comment> commentsJson) {
        ArrayList<Comment> comments = new ArrayList<>();

        for (Comment commentJson : commentsJson) {
            Comment comment = new Comment();

            comment.setCommentID(commentJson.getCommentID());
            comment.setUserID(commentJson.getUserID());
            comment.setText(commentJson.getText());
            comment.setImage(commentJson.getImage());

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                comment.setDate(dateFormat.parse(commentJson.getLastUpdate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (commentJson.getUserID() == Variables.User.getUserID()) {
                comment.setUserName(Variables.User.getName());
                comment.setUserImage(Variables.User.getImageBase64());
            } else {
                Contact contact = getContactByID(commentJson.getUserID());
                if (contact != null) {
                    comment.setUserName(contact.getName());
                    comment.setUserImage(contact.getImageBase64());
                }
            }

            comments.add(comment);
        }

        return comments;
    }

    private static Contact getContactByID(int id) {
        for (Contact contact : Variables.contactsWithApp)
            if (contact.getUserID() == id)
                return contact;
        return null;
    }

}
