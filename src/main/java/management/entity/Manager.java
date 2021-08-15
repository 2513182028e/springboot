package management.entity;


import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class Manager  implements Serializable {
    private String ManagerID;
    private String password;
    private String UserName;
    private int length;
    private String salt;


    public Manager() {
    }

    @Override
    public String toString() {
        return "Manager{" +
                "ManagerID='" + ManagerID + '\'' +
                ", password='" + password + '\'' +
                ", UserName='" + UserName + '\'' +
                ", length=" + length +
                ", salt='" + salt + '\'' +
                '}';
    }

    public String getManagerID() {
        return ManagerID;
    }

    public void setManagerID(String managerID) {
        ManagerID = managerID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Manager(String managerID, String password, String userName, int length, String salt) {
        ManagerID = managerID;
        this.password = password;
        UserName = userName;
        this.length = length;
        this.salt = salt;
    }
}
