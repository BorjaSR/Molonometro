<?php
/**
 * Database configuration
 */
 
define('DB_USERNAME', 'root');
define('DB_PASSWORD', '');
define('DB_HOST', 'localhost');
define('DB_NAME', 'db_molonometro');






//define("GOOGLE_API_KEY", "AIzaSyDsW9f8Oz3QxoiCEM9Zz6P994Wc0PgpRBc");
define("FIREBASE_API_KEY", "AAAA861W9Qo:APA91bFCvB1HENb6KcbY7fkqm1KTV1X4kwHw46Z25ENF3zqP3J7P03q3CgktN9IL23A9-vklj0zB1GaJ3CFG9hGo0FFDREqoWaEhN3Ja7K4sgzbcrK73CBQuE37czDyDjhhdxt5pVLrz");
define('FIREBASE_TOPICS_PREFIX', "DES_");
define('SALT', 'mi_sal_superreshulona');

define('PUSH_FLAG_NEW_GROUP', 0);
define('PUSH_FLAG_COMMENT', 1);

define('ACTIVATION_EMAIL', "<html xmlns=\"http://www.w3.org/1999/xhtml\">
<head>
<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />
<title>Untitled Document</title>
</head>
<body>

<div style=\"width:100%;\" align=\"center\">

<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">
  <tr>
    <td align=\"center\" valign=\"top\" style=\"background-color:#FFF;\" bgcolor=\"#FFF;\">
        <table width=\"600\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">
          <tr>
            <td width=\"20\" align=\"left\" valign=\"top\" bgcolor=\"#009688\" style=\"background-color:#009688;\">&nbsp;</td>
            <td align=\"center\" valign=\"top\" bgcolor=\"#009688\" style=\"background-color:#009688; color:#7b7b7b; font-family:Arial, Helvetica, sans-serif; font-size:14px;\"><br>
              <br>
            <br>
            <div style=\"color:#fff; font-family:Arial, Helvetica, sans-serif; font-size:24px;\">
              Molonometro<br>
                !Gracias por registrarte en nuestra App!
            </div><br>
                <br>
                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">
                  <tr>
                    <!-- <td align=\"center\" valign=\"middle\"><img src=\"images/divider.gif\" alt=\"\" width=\"544\" height=\"5\"></td> -->
                    <a style=\"padding-top:15px; padding-bottom:15px; padding-left: 25px; padding-right:25px; color:#fff; background-color:#FF5722; font-family:Arial, Helvetica, sans-serif; font-size:14px;\"
                    href=\"http://192.168.1.142:8888/molonometro/v1/activation?activationCode=999888777666\">Activa tu cuenta aquí</a>
                  </tr>
                </table>
                <br>
                <br>
            <div style=\"margin-top: 30px; color:#424242; font-family:Arial, Helvetica, sans-serif; font-size:12px;\"><b>Company Address</b> <br>
              Contact Person <br>
              Phone: (123) 456-789 <br>
              Email: company@companyname.com <br>
              Website: www.companywebsite.com</div>
            <br>
            <br>
            <br></td>
            <td width=\"20\" align=\"left\" valign=\"top\" bgcolor=\"#009688\" style=\"background-color:#009688;\">&nbsp;</td>
          </tr>
        </table>
    </td>
  </tr>
</table>

</div>

</body>
</html>");

?>
