<?php

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 *
 * @author Ravi Tamada
 * @link URL Tutorial link
 */
class LikesDAO {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . '/../../include/db_connect.php';
        require_once dirname(__FILE__) . '/./UserDAO.php';
        require_once dirname(__FILE__) . '/./CommentsDAO.php';
        // opening db connection
    }


    // creating new user if not existed
    public function addLike($commentID, $userID) {
        $userDAO = new UserDAO();
        $commentsDAO = new CommentsDAO();

        if ($userDAO->isUserExistsById($userID) &&
            $commentsDAO->isCommentExistById($commentID)) {

            // insert query
            $db = new DbConnect();
            $this->conn = $db->connect();


            $response = array();

            $stmt = $this->conn->prepare("INSERT INTO likes(CommentID, UserID, Created, Deleted) values(?, ?, now(), 0)");
            $stmt->bind_param("ii", $commentID, $userID);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {

                // User successfully inserted
                $response["status"] = 200;
                $response["like"] = true;
            } else {
                // Failed to create user
                $response["status"] = 430;
                $response["error"] = "Oops! An error occurred while put like";
            }
        } else {
            $response["status"] = 432;
            $response["error"] = "The user or comment doesn't exists";
        }

        $db->disconnect();
        return $response;
    }

    public function getLikesByComment($commentID) {
        $db = new DbConnect();
        $this->conn = $db->connect();
        
        $stmt = $this->conn->prepare("SELECT UserID From likes where CommentID = ? and Deleted = 0 ORDER BY Created DESC");
        $stmt->bind_param("i", $commentID);
        
        $response = array();

        if ($stmt->execute()) {
            $stmt->bind_result($UserID);

            $likes = array();
            $i = 0;
            while ($stmt->fetch()) {
                $likes[$i] = $UserID;
                $i++;
            }

            $stmt->close();

            // User successfully inserted
            $response["status"] = 200;
            $response["likes"] = $likes;

        } else {
            // User successfully inserted
            $response["status"] = 457;
            $response["error"] = "Oops! An error occurred while getting the comments";
        }

        $db->disconnect();
        return $response;
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
}
?>