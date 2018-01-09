package com.bsalazar.molonometro.rest.services;

import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.entities.Contact;
import com.bsalazar.molonometro.entities.FriendRquest;
import com.bsalazar.molonometro.entities.Group;
import com.bsalazar.molonometro.entities.LastEvent;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.entities.User;
import com.bsalazar.molonometro.general.Variables;
import com.bsalazar.molonometro.rest.json.ContactJson;
import com.bsalazar.molonometro.rest.json.FriendRequestJson;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.json.LastEventJson;
import com.bsalazar.molonometro.rest.json.UserJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Borja on 29/10/2016.
 */

public class Parser {

    public static User parseUser(UserJson userJson) {
        User user = new User();

        user.setUserID(userJson.getUserID());
        user.setUserName(userJson.getUserName());
        user.setEmail(userJson.getEmail());
        user.setName(userJson.getName());
        user.setPhone(userJson.getPhone());
        user.setState(userJson.getState());
        user.setImageURL(userJson.getImage());
        user.setNumRequest(userJson.getRequests());

        return user;
    }

    public static Contact parseContact(ContactJson contactJson) {
        Contact contact = new Contact();

        contact.setUserID(contactJson.getUserID());
        contact.setUserName(contactJson.getUserName());
        contact.setEmail(contactJson.getEmail());
        contact.setName(contactJson.getName());
        contact.setPhone(contactJson.getPhone());
        contact.setState(contactJson.getState());
        contact.setImageURL(contactJson.getImage());

        return contact;
    }


    public static Group parseGroup(GroupJson groupJson) {
        Group group = new Group();
        group.setId(groupJson.getGroupID());
        group.setName(groupJson.getName());
        group.setImageURL(groupJson.getImage());
        group.setFirebaseTopic(groupJson.getFirebaseTopic());

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
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
        lastEvent.setUser(parseParticipant(lastEventJson.getUserID()));
        lastEvent.setDestinationUser(parseParticipant(lastEventJson.getDestinationUserID()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            lastEvent.setLastUpdate(dateFormat.parse(lastEventJson.getLastUpdate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return lastEvent;
    }

    private static ArrayList<Participant> parseParticipants(ArrayList<Participant> participantsJson) {
        ArrayList<Participant> participants = new ArrayList<>();

        for (Participant participantJson : participantsJson)
            participants.add(parseParticipant(participantJson));

        return participants;
    }

    private static Participant parseParticipant(Participant participantJson){
        Participant participant = new Participant();
        participant.setUserID(participantJson.getUserID());
        participant.setUserName(participantJson.getUserName());
        participant.setImage(participantJson.getImage());
        participant.setName(participantJson.getName());
        participant.setState(participantJson.getState());
        participant.setMolopuntos(participantJson.getMolopuntos());
        participant.setAdmin(participantJson.isAdmin());
        return participant;
    }

    public static Participant parseUserToParticipant(User user){
        Participant participant = new Participant();
        participant.setUserID(user.getUserID());
        participant.setUserName(user.getUserName());
        participant.setImage(user.getImageURL());
        participant.setName(user.getName());
        participant.setState(user.getState());
        return participant;
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
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
                comment.setUserName(Variables.User.getUserName());
                comment.setUserImage(Variables.User.getImageURL());
                comment.setDestinationUserName(getContactInGroupByID(commentJson.getDestinationUserID(), Variables.Group).getUserName());
            } else {
                Participant contact = getContactInGroupByID(commentJson.getUserID(), Variables.Group);
                if (contact != null) {
                    comment.setUserName(contact.getUserName());
                    comment.setUserImage(contact.getImage());

                    if (commentJson.getDestinationUserID() == Variables.User.getUserID())
                        comment.setDestinationUserName(Variables.User.getUserName());
                    else
                        comment.setDestinationUserName(getContactInGroupByID(commentJson.getDestinationUserID(), Variables.Group).getUserName());

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
                comment.setUserName(Variables.User.getUserName());
                comment.setUserImage(Variables.User.getImageURL());
            } else {
                Participant participant = getContactInGroupByID(commentJson.getUserID(), Variables.Group);
                if (participant != null) {
                    comment.setUserName(participant.getUserName());
                    comment.setUserImage(participant.getImage());
                }
            }

            comments.add(comment);
        }

        return comments;
    }

    private static Participant getContactInGroupByID(int contactId, Group group) {
        for (Participant participant : group.getParticipants())
            if (participant.getUserID() == contactId)
                return participant;
        return null;
    }

    public static FriendRquest parseRequest(FriendRequestJson friendRequestJson){
        FriendRquest friendRequest = new FriendRquest();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            friendRequest.setDate(dateFormat.parse(friendRequestJson.getLastUpdate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        friendRequest.setContact(parseContact(friendRequestJson.getUser()));

        return friendRequest;
    }
}
