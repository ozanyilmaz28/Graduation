package entities;

/**
 * Created by LA-173 on 24.03.2016.
 */
public class UserInfo {

    public static String UserName;
    public static String Email;
    public static String Phone;
    public static int UserID;

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static String getEmail() {
        return Email;
    }

    public static void setEmail(String email) {
        Email = email;
    }

    public static String getPhone() {
        return Phone;
    }

    public static void setPhone(String phone) {
        Phone = phone;
    }

    public static int getUserID() {
        return UserID;
    }

    public static void setUserID(int userID) {
        UserID = userID;
    }
}
