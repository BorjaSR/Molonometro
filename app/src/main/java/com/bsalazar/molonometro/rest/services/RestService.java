package com.bsalazar.molonometro.rest.services;

import com.bsalazar.molonometro.entities.Comment;
import com.bsalazar.molonometro.entities.Participant;
import com.bsalazar.molonometro.rest.json.AddUserToGroupJson;
import com.bsalazar.molonometro.rest.json.ContactJson;
import com.bsalazar.molonometro.rest.json.ContactsListJson;
import com.bsalazar.molonometro.rest.json.CreateGroupJson;
import com.bsalazar.molonometro.rest.json.CreateUserJson;
import com.bsalazar.molonometro.rest.json.FriendRequestJson;
import com.bsalazar.molonometro.rest.json.GetContactDetailJson;
import com.bsalazar.molonometro.rest.json.GroupJson;
import com.bsalazar.molonometro.rest.json.LikeJson;
import com.bsalazar.molonometro.rest.json.PushTestJson;
import com.bsalazar.molonometro.rest.json.RequestFriendJson;
import com.bsalazar.molonometro.rest.json.SearchJson;
import com.bsalazar.molonometro.rest.json.UpdateUserJson;
import com.bsalazar.molonometro.rest.json.UserIdJson;
import com.bsalazar.molonometro.rest.json.UserJson;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by smesonero on 05/10/2015.
 */

public interface RestService {

    // USER //
    @POST("/user/login")
    void login(
            @Body UserJson userJson,
            Callback<Integer> callback);

    @POST("/user/createUserNew")
    void createUser(
            @Body CreateUserJson createUserJson,
            Callback<UserJson> callback);

    @POST("/user/updateUser")
    void updateUser(
            @Body UpdateUserJson updateUserJson,
            Callback<UserJson> callback);

    @POST("/user/updateUserImage")
    void updateUserImage(
            @Body UpdateUserJson updateUserJson,
            Callback<UserJson> callback);

    @POST("/user/updateFirebaseToken")
    void updateFirebaseToken(
            @Body UpdateUserJson updateUserJson,
            Callback<Boolean> callback);

    @POST("/user/getUser")
    void getUser(
            @Body UpdateUserJson updateUserJson,
            Callback<UserJson> callback);

    @POST("/user/searchUsers")
    void searchUsers(
            @Body SearchJson updateUserJson,
            Callback<List<ContactJson>> callback);

    @POST("/user/requestFriendship")
    void requestFriendship(
            @Body RequestFriendJson updateUserJson,
            Callback<Boolean> callback);

    @POST("/user/getRequest")
    void getRequest(
            @Body UserJson userJson,
            Callback<List<FriendRequestJson>> callback);



    // CONTACTS //
    @POST("/contact/checkContacts")
    void checkContacts(
            @Body ContactsListJson updateUserJson,
            Callback<List<ContactJson>> callback);

    @POST("/contact/getContactByID")
    void getContactByID(
            @Body GetContactDetailJson updateUserJson,
            Callback<ContactJson> callback);



    // GROUP //
    @POST("/group/createGroup")
    void createGroup(
            @Body CreateGroupJson createGroupJson,
            Callback<GroupJson> callback);

    @POST("/group/updateGroup")
    void updateGroup(
            @Body GroupJson updateGroupJson,
            Callback<GroupJson> callback);

    @POST("/group/updateGroupImage")
    void updateGroupImage(
            @Body GroupJson updateGroupJson,
            Callback<GroupJson> callback);

    @POST("/group/getGroupsByUser")
    void getGroupsByUser(
            @Body UserIdJson userIdJson,
            Callback<List<GroupJson>> callback);

    @POST("/group/getGroupByID")
    void getGroupByID(
            @Body GroupJson userIdJson,
            Callback<GroupJson> callback);

    @POST("/group/getGroupParticipantsByID")
    void getGroupParticipantsByID(
            @Body GroupJson groupIdJson,
            Callback<ArrayList<Participant>> callback);

    @POST("/group/addUserToGroup")
    void addUserToGroup(
            @Body AddUserToGroupJson addUserToGroupJson,
            Callback<Boolean> callback);

    @POST("/group/makeUserAdmin")
    void makeUserAdmin(
            @Body AddUserToGroupJson addUserToGroupJson,
            Callback<Boolean> callback);

    @POST("/group/removeUserFromGroup")
    void removeUserFromGroup(
            @Body AddUserToGroupJson addUserToGroupJson,
            Callback<Boolean> callback);



    // COMENTS //
    @POST("/comments/getCommentByGroup")
    void getCommentByGroup(
            @Body GroupJson groupIdJson,
            Callback<ArrayList<Comment>> callback);

    @POST("/comments/getRepliesByComment")
    void getRepliesByComment(
            @Body Comment groupIdJson,
            Callback<ArrayList<Comment>> callback);

    @POST("/comments/addCommentToGroup")
    void addCommentToGroup(
            @Body Comment comment,
            Callback<Comment> callback);

    @POST("/comments/addReplyToComment")
    void addReplyToComment(
            @Body Comment comment,
            Callback<Boolean> callback);


    // LIKES
    @POST("/likes/addLikeToComment")
    void addLikeToComment(
            @Body LikeJson likeJson,
            Callback<Boolean> callback);


    @POST("/dummyService")
    void dummyService(
            @Body PushTestJson userIdJson,
            Callback<String> callback);

    @POST("/fcm/sendPushTo")
    void sendPushTest(
            @Body PushTestJson userIdJson,
            Callback<Boolean> callback);

}



