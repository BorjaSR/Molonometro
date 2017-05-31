<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class CommentsDAO {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/../../include/db_connect.php';
        require_once dirname(__FILE__) . '/./UserDAO.php';
        require_once dirname(__FILE__) . '/./GroupDAO.php';
        // opening db connection
    }


    // creating new user if not existed
    public function addComment($groupID, $userID, $destinationUserID, $text, $image) {
        $userDAO = new UserDAO();
        $groupDAO = new GroupDAO();
        if ($userDAO->isUserExistsById($userID) &&
            $userDAO->isUserExistsById($destinationUserID) &&
            $groupDAO->isGroupExistsById($groupID)) {


            $response = array();

            // insert query
        $db = new DbConnect();
        $this->conn = $db->connect();

            $stmt = $this->conn->prepare("INSERT INTO comments(GroupID, UserID, DestinationUserID, Text, Image, Created, LastUpdate, Deleted) values(?, ?, ?, ?, ?, now(), now(), 0)");
            $stmt->bind_param("iiiss", $groupID, $userID, $destinationUserID, $text, $image);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {

                // User successfully inserted
                $response["status"] = 200;
                $response["comment"] = $this->getLastCommentByUserInGroup($userID, $groupID);
            } else {
                // Failed to create user
                $response["status"] = 430;
                $response["error"] = "Oops! An error occurred while create group";
            }
        } else {
            $response["status"] = 432;
            $response["error"] = "The user doesn't exists";
        }

        $db->disconnect();
        return $response;
    }

    // creating new user if not existed
    public function addReply($groupID, $userID, $destinationUserID, $text, $associatedCommentID) {


        $userDAO = new UserDAO();
        $groupDAO = new GroupDAO();
        if ($userDAO->isUserExistsById($userID) &&
            $userDAO->isUserExistsById($destinationUserID) &&
            $groupDAO->isGroupExistsById($groupID)) {


            $response = array();

            // insert query
        $db = new DbConnect();
        $this->conn = $db->connect();
            $stmt = $this->conn->prepare("INSERT INTO comments(GroupID, UserID, DestinationUserID, AssociatedCommentID, Text, Created, LastUpdate, Deleted) values(?, ?, ?, ?, ?, now(), now(), 0)");
            $stmt->bind_param("iiiis", $groupID, $userID, $destinationUserID, $associatedCommentID, $text);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {

                $this->setCommentWithAnswers($associatedCommentID);

                // User successfully inserted
                $response["status"] = 200;
                $response["reply"] = true;
            
            } else {
                // Failed to create user
                $response["status"] = 430;
                $response["error"] = "Oops! An error occurred while add reply";
            }
        } else {
            $response["status"] = 432;
            $response["error"] = "The user doesn't exists";
        }

        $db->disconnect();
        return $response;
    }


    public function isCommentExistById($commentID) {
        $db = new DbConnect();
        $this->conn = $db->connect();

        $stmt = $this->conn->prepare("SELECT CommentID from comments WHERE CommentID = ? and Deleted = 0");
        $stmt->bind_param("i", $commentID);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        $db->disconnect();
        return $num_rows > 0;
    }


    public function getCommentByGroup($groupID) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        
        $stmt = $this->conn->prepare("SELECT CommentID, UserID, DestinationUserID, hasAnswers, Text, Image, Created From comments where GroupID = ? and AssociatedCommentID IS NULL and Deleted = 0 ORDER BY Created DESC");
        $stmt->bind_param("i", $groupID);
        
        $response = array();

        if ($stmt->execute()) {
            $stmt->bind_result($CommentID, $UserID, $DestinationUserID, $hasAnswers, $Text, $Image, $LastUpdate);

            $commentsList = array();
            $i = 0;
            while ($stmt->fetch()) {


                $comment = array();
                $comment["CommentID"] = $CommentID;
                $comment["UserID"] = $UserID;
                $comment["DestinationUserID"] = $DestinationUserID;
                if($hasAnswers == 0){
                    $comment["hasAnswers"] = false;
                }
                else{
                    $comment["hasAnswers"] = true;
                }

                $comment["Text"] = $Text;
                $comment["Image"] = $Image;
                $comment["LastUpdate"] = $LastUpdate;
                $comment["Comments"] = $this->getSimpleReplies($CommentID);

                $likesDAO = new LikesDAO();
                $DBresponse = $likesDAO->getLikesByComment($CommentID);
                $comment["Likes"] = $DBresponse["likes"];


                $commentsList[$i] = $comment;
                $i++;
            }

            $stmt->close();

            // User successfully inserted
            $response["status"] = 200;
            $response["comments"] = $commentsList;

        } else {
            // User successfully inserted
            $response["status"] = 457;
            $response["error"] = "Oops! An error occurred while getting the comments";
        }

        $db->disconnect();
        return $response;
    }


    public function getLastCommentByUserInGroup($userID, $groupID){
        //SELECT CommentID, UserID, DestinationUserID, LastUpdate From comments where GroupID = 15 and AssociatedCommentID IS NULL and Deleted = 0 ORDER BY Created DESC LIMIT 1
        $db = new DbConnect();
        $this->conn = $db->connect();
        $response = array();

        $stmt = $this->conn->prepare("SELECT CommentID, DestinationUserID, Text, Created, LastUpdate From comments where GroupID = ? and UserID = ? and AssociatedCommentID IS NULL and Deleted = 0 ORDER BY Created DESC LIMIT 1");
        $stmt->bind_param("ii", $groupID, $userID);
        
        if ($stmt->execute()) {
            $stmt->bind_result($CommentID, $DestinationUserID, $Text, $Created, $LastUpdate);
            $stmt->fetch();

            $comment = array();
            $comment["CommentID"] = $CommentID;
            $comment["GroupID"] = $groupID;
            $comment["UserID"] = $userID;
            $comment["DestinationUserID"] = $DestinationUserID;
            $comment["Text"] = $Text;
            $comment["LastUpdate"] = $LastUpdate;
            $comment["Created"] = $Created;
        }


        $stmt->close();
        $db->disconnect();
        return $comment;
    }

    public function getLastCommentByGroup($groupID){
        //SELECT CommentID, UserID, DestinationUserID, LastUpdate From comments where GroupID = 15 and AssociatedCommentID IS NULL and Deleted = 0 ORDER BY Created DESC LIMIT 1
        $db = new DbConnect();
        $this->conn = $db->connect();
        $response = array();

        $stmt = $this->conn->prepare("SELECT CommentID, UserID, DestinationUserID, LastUpdate From comments where GroupID = ? and AssociatedCommentID IS NULL and Deleted = 0 ORDER BY Created DESC LIMIT 1");
        $stmt->bind_param("i", $groupID);
        
        if ($stmt->execute()) {
            $stmt->bind_result($CommentID, $UserID, $DestinationUserID, $LastUpdate);
            $stmt->fetch();

            $comment = array();
            $comment["CommentID"] = $CommentID;
            $comment["UserID"] = $UserID;
            $comment["DestinationUserID"] = $DestinationUserID;
            $comment["LastUpdate"] = $LastUpdate;
        }


        $stmt->close();
        $db->disconnect();
        return $comment;
    }

    public function getSimpleReplies($commentID) {
        $db = new DbConnect();
        $this->conn = $db->connect();

        $stmt = $this->conn->prepare("SELECT UserID FROM `comments` WHERE AssociatedCommentID = ?");
        $stmt->bind_param("i", $commentID);
        
        $response = array();

        if ($stmt->execute()) {
            $stmt->bind_result($UserID);

            $i = 0;
            while ($stmt->fetch()) {
                $response[$i] = $UserID;
                $i++;
            }

            $stmt->close();
        }

        $db->disconnect();
        return $response;
    }


    public function getRepliesByComment($commentID) {
        

        if($this->isCommentExistById($commentID)){
            $db = new DbConnect();
            $this->conn = $db->connect();
            $stmt = $this->conn->prepare("SELECT CommentID, UserID, Text, Image From comments where AssociatedCommentID = ? and Deleted = 0 ORDER BY Created DESC");
            $stmt->bind_param("i", $commentID);
            
            $response = array();

            if ($stmt->execute()) {
                $stmt->bind_result($CommentID, $UserID, $Text, $Image);

                $repliesList = array();
                $i = 0;
                while ($stmt->fetch()) {


                    $reply = array();
                    $reply["CommentID"] = $CommentID;
                    $reply["UserID"] = $UserID;
                    $reply["Text"] = $Text;
                    $reply["Image"] = $Image;

                    $repliesList[$i] = $reply;
                    $i++;
                }

                $stmt->close();

                // User successfully inserted
                $response["status"] = 200;
                $response["replies"] = $repliesList;

            } else {
                // User successfully inserted
                $response["status"] = 457;
                $response["error"] = "Oops! An error occurred while getting the comments";
            }
        }else {
            // User successfully inserted
            $response["status"] = 458;
            $response["error"] = "Comment doesn't exist";
        }
        $db->disconnect();
        return $response;
    }

    public function getCommentById($commentID) {
        
        if($this->isCommentExistById($commentID)){
        $db = new DbConnect();
        $this->conn = $db->connect();
            $stmt = $this->conn->prepare("SELECT GroupID, UserID, DestinationUserID, Text From comments where CommentID = ? and Deleted = 0");
            $stmt->bind_param("i", $commentID);
            
            if ($stmt->execute()) {
                $stmt->bind_result($GroupID, $UserID, $DestinationUserID, $Text);
                $stmt->fetch();
                $comment = array();
                $comment["CommentID"] = $commentID;
                $comment["GroupID"] = $GroupID;
                $comment["UserID"] = $UserID;
                $comment["DestinationUserID"] = $DestinationUserID;
                $comment["Text"] = $Text;
                $stmt->close();
        $db->disconnect();
                return $comment;
            } else {
        $db->disconnect();
                return NULL;
            }
        } else {
        $db->disconnect();
            return NULL;
        }
    }


    // creating new user if not existed
    public function setCommentWithAnswers($commentID) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        

        $response = array();
        
        // update query
        $stmt = $this->conn->prepare("UPDATE comments SET hasAnswers = 1, LastUpdate = now() WHERE CommentID = ?");
        $stmt->bind_param("i", $commentID);

        $result = $stmt->execute();
        $stmt->close();

        if ($result) {
        $db->disconnect();
            return true;
        } else {
        $db->disconnect();
            return false;
        }
    }
}
?>